package unicauca.sing4all.ui.listen

import android.app.Activity
import android.arch.lifecycle.ViewModelProvider
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.*
import android.content.pm.PackageManager
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import android.speech.tts.TextToSpeech
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.*
import com.jakewharton.rxbinding2.view.clicks
import com.jakewharton.rxbinding2.widget.textChanges
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import kotlinx.android.synthetic.main.activity_connection_acvitivity.*
import kotlinx.android.synthetic.main.activity_listen.*
import org.jetbrains.anko.toast
import unicauca.sing4all.ConnectionAcvitivity
import unicauca.sing4all.Manifest
import unicauca.sing4all.R
import unicauca.sing4all.databinding.ActivityListenBinding
import unicauca.sing4all.di.Injectable
import unicauca.sing4all.quantifier.Hand
import unicauca.sing4all.ui.adapter.WordListenAdapter
import unicauca.sing4all.util.LifeDisposable
import unicauca.sing4all.util.applySchedulers
import unicauca.sing4all.util.buildViewModel
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.io.UnsupportedEncodingException
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ListenActivity : AppCompatActivity(), Injectable {


    @Inject
    lateinit var factory: ViewModelProvider.Factory
    val viewModel: ListenViewModel by lazy { buildViewModel<ListenViewModel>(factory) }
    private val btAddress: String by lazy { viewModel.getBtSession() }
    @Inject
    lateinit var adapter: WordListenAdapter

    @Inject
    lateinit var textToSpeech: TextToSpeech

    @Inject
    lateinit var uxTask: ListenTask

    val dis: LifeDisposable = LifeDisposable(this)
    var uxDis: Disposable? = null

    var testing = false
    var listen = false

    lateinit var binding: ActivityListenBinding

    var word = ""
        set(value) {
            field = value
            sign?.setText(value)
        }

    //region BTComponents
    private var mBTAdapter: BluetoothAdapter? = null
    private val TAG = ConnectionAcvitivity::class.java.simpleName
    private var mHandler: Handler? = null // Our main handler that will receive callback notifications
    private var mConnectedThread: ConnectedThread? = null // bluetooth background worker thread to send and receive data
    private var mBTSocket: BluetoothSocket? = null // bi-directional client-to-client data path

    //endregion

    //region FuncionCreateBTThread
    fun threadBT( address: String){
    object : Thread() {
        override fun run() {
            var fail = false

            val device = mBTAdapter!!.getRemoteDevice(address)

            try {
                mBTSocket = createBluetoothSocket(device)
            } catch (e: IOException) {
                fail = true
                Toast.makeText(baseContext, "Socket creation failed", Toast.LENGTH_SHORT).show()
            }

            // Establish the Bluetooth socket connection.
            try {
                mBTSocket!!.connect()
            } catch (e: IOException) {
                try {
                    fail = true
                    mBTSocket!!.close()
                    mHandler!!.obtainMessage(CONNECTING_STATUS, -1, -1)
                            .sendToTarget()
                } catch (e2: IOException) {
                    //insert code to deal with this
                    Toast.makeText(baseContext, "Socket creation failed", Toast.LENGTH_SHORT).show()
                }

            }

            if (!fail) {
                mConnectedThread = ConnectedThread(mBTSocket!!)
                mConnectedThread!!.start()

                mHandler!!.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                        .sendToTarget()
            }
        }
    }.start()
}
    //endregion




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBTAdapter = BluetoothAdapter.getDefaultAdapter() // get a handle on the bluetooth radio
        binding = DataBindingUtil.setContentView(this, R.layout.activity_listen)

        title = "Traductor"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.letters = emptyList()
        binding.listen = false

        list.adapter = adapter
        list.layoutManager = LinearLayoutManager(this)

        //Here is message from arduino
        mHandler = object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                if (msg.what == MESSAGE_READ) {
                    var readMessage: String? = null
                    var sensorValues : List<String> = emptyList()

                    try {
                        readMessage = String(msg.obj as ByteArray, Charsets.UTF_8)
                        sensorValues = readMessage.split(";")
                        var menique =  sensorValues[0].toInt()
                        var anular = sensorValues[1].toInt()
                        var medio = sensorValues[2].toInt()
                        var indice = sensorValues[3].toInt()
                        var pulgar = sensorValues[4].toInt()
                        var hand:Hand = Hand(menique,anular,medio,indice,pulgar)
                     //       toast(""+menique)
                        if (listen){
                        if(!testing) runListen(listOf(hand))}
                    } catch (e: UnsupportedEncodingException) {
                        e.printStackTrace()
                    }


                }

                if (msg.what == CONNECTING_STATUS) {
                    Log.e("Conectado", "Successfull")
                }
            }
        }
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.listen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, Data: Intent?) {
        // Check which request we're responding to
        if (requestCode == REQUEST_ENABLE_BT) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.
                bluetoothStatus.text = "Enabled"
            } else
                bluetoothStatus.text = "Disabled"
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_task -> {
                testing = true
                toast("Test de Usuario Activado")
            }
            R.id.action_no_task -> {
                stopUxTask()
                testing = false
                toast("Test de Usuario Desactivado")
            }
            else -> finish()
        }
        return super.onOptionsItemSelected(item)
    }



    override fun onResume() {

        super.onResume()
        threadBT(btAddress)

        dis add  viewModel.NeuralNNResult(16603f,64373f,35285f,51752f).
                        subscribeBy(onSuccess = {
                            it.result
                        },
                                onError = {
                                    toast(it.message!!)})


        dis add adapter.onClick
                .subscribe {
                    textToSpeech.speak(it.text, TextToSpeech.QUEUE_FLUSH, null, null)
                }

        dis add btnPlay.clicks()
                .subscribe {
                    listen= true
                    binding.listen = true
                    if (testing) runUxTask()
                }

        dis add btnStop.clicks()
                .subscribe {
                    listen=false
                    stopListen()
                    if (testing) stopUxTask()
                }

        dis add sign.textChanges()
                .filter { !binding.listen }
                .doOnNext { if (it == "") adapter.data = emptyList() }
                .filter { it != "" }
                .flatMapSingle {
                    viewModel.queryWords(it.toString())
                }
                .subscribe {
                    adapter.data = it
                }

        dis add Observable.merge(
                btnChar1.clicks().map { 0 },
                btnChar2.clicks().map { 1 },
                btnChar3.clicks().map { 2 }
        )
                .map {
                    word = word.replaceRange(word.lastIndex, word.length, binding.letters!![it])
                    sign.setText(word)
                    binding.letters = emptyList()
                    textToSpeech.speak(binding.letters!![it], TextToSpeech.QUEUE_FLUSH, null, null)
                    word
                }
                .flatMapSingle(viewModel::queryWords)
                .subscribe { adapter.data = it }

        dis add btnClear.clicks()
                .subscribe {
                    adapter.data = emptyList()
                    word = ""
                }

    }

    fun runUxTask() {
        uxDis = uxTask.mockHand()
                .flatMapSingle(viewModel::calculateLetter)
                .filter { it.isNotEmpty() }
                .doOnNext {
                    word += it[0]
                    // Solo dice la letra cuando es una opc, en caso contrario al seleccionar la opcion
                    if(it.size == 1){
                        textToSpeech.speak(it[0], TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                    binding.letters = it
                }
                .map { word }
                .flatMapSingle(viewModel::queryWords)
                .subscribe {

                    adapter.data = it
                }
    }



    fun stopUxTask() {
        uxDis?.dispose()
        uxDis = null
        stopListen()
    }

    fun stopListen() {
        binding.listen = false
        binding.letters = emptyList()

    }


    fun getHand(btfingers:List<Hand>): Observable<Hand> = btfingers.toObservable()
            .concatMap{i-> Observable.just(i).delay(2, TimeUnit.SECONDS)}
            .applySchedulers()

    fun runListen(fingers:List<Hand>) {
        uxDis = getHand(fingers)
                .flatMapSingle(viewModel::calculateLetter)
                .filter { it.isNotEmpty() }
                .doOnNext {
                    word += it[0]
                    binding.letters = it
                }
                .map { word }
                .flatMapSingle(viewModel::queryWords)
                .subscribe {
                    adapter.data = it
                    mConnectedThread!!.write("1")
                }
    }



    @Throws(IOException::class)
    private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {
        try {
            val m = device.javaClass.getMethod("createInsecureRfcommSocketToServiceRecord", UUID::class.java)
            return m.invoke(device, BTMODULEUUID) as BluetoothSocket
        } catch (e: Exception) {
            Log.e(TAG, "Could not create Insecure RFComm Connection", e)
        }

        return device.createRfcommSocketToServiceRecord(BTMODULEUUID)
    }


    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {
        private val mmInStream: InputStream?
        private val mmOutStream: OutputStream?

        init {
            var tmpIn: InputStream? = null
            var tmpOut: OutputStream? = null

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = mmSocket.inputStream
                tmpOut = mmSocket.outputStream
            } catch (e: IOException) {
            }

            mmInStream = tmpIn
            mmOutStream = tmpOut
        }

        override fun run() {
            var buffer: ByteArray  // buffer store for the stream
            var bytes: Int // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream!!.available()
                    if (bytes != 0) {
                        buffer = ByteArray(1024)
                        SystemClock.sleep(100) //pause and wait for rest of data. Adjust this depending on your sending speed.
                        // how many bytes are ready to be read?
                        bytes = mmInStream.available() // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes) // record how many bytes we actually read
                        Log.d("Datos", bytes.toString() + "")
                        mHandler!!.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget() // Send the obtained bytes to the UI activity
                    }
                } catch (e: IOException) {
                    e.printStackTrace()

                    break
                }

            }
        }

        /* Call this from the main activity to send data to the remote device */
        fun write(input: String) {

            val bytes = input.toByteArray()           //converts entered String into bytes
            try {
                mmOutStream!!.write(bytes)
            } catch (e: IOException) {
            }

        }

        /* Call this from the main activity to shutdown the connection */
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
            }

        }
    }

    companion object {

        private val BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB") // "random" unique identifier
        private val REQUEST_ENABLE_BT = 1 // used to identify adding bluetooth names
        private val MESSAGE_READ = 2 // used in bluetooth handler to identify message update
        private val CONNECTING_STATUS = 3 // used in bluetooth handler to identify message status
    }


}
