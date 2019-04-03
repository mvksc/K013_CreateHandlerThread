package m.vk.k013_createhandlerthread

import android.os.*
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var backgroundHandlerThread: HandlerThread
    lateinit var backgroundHandler: Handler
    lateinit var mainHandler: Handler
    var isLooper : Boolean = true
    var countRed : Int = 30
    var countOrange: Int = 5
    var countGreen : Int = 30

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //สร้าง Looper background ขึ้นมาใหม่ ชื่อ BackgroundHandlerThread พร้อมเริ่มทำงานทันที
        backgroundHandlerThread = HandlerThread("BackgroundHandlerThread")
        backgroundHandlerThread.start()

        //ผูก Looper background เข้ากับ Handler background
        backgroundHandler = object : Handler(backgroundHandlerThread.looper){
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                //Run with background
                var msgMain = Message()
                msgMain.obj = msg!!.obj
                mainHandler.sendMessageDelayed(msgMain,1000) //delayed on background

            }
        }

        //ผูก Looper Main UI เข้ากับ Handler Main UI
        mainHandler = object : Handler(Looper.getMainLooper()){
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                //Run with Main Thread
                    when {
                        countGreen >= 0 -> {
                            tvTimeCount.text = String.format("%02d",countGreen--)
                            imgRed.setImageResource(R.color.color_gray)
                            imgOrange.setImageResource(R.color.color_gray)
                            imgGreen.setImageResource(R.color.color_green)
                        }
                        countOrange >= 0 -> {
                            tvTimeCount.text = String.format("%02d",countOrange--)
                            imgRed.setImageResource(R.color.color_gray)
                            imgOrange.setImageResource(R.color.color_orange)
                            imgGreen.setImageResource(R.color.color_gray)
                        }
                        countRed >= 0 -> {
                            tvTimeCount.text = String.format("%02d",countRed--)
                            imgRed.setImageResource(R.color.color_rad)
                            imgOrange.setImageResource(R.color.color_gray)
                            imgGreen.setImageResource(R.color.color_gray)
                        }
                        else -> {
                            countRed = 30
                            countOrange = 5
                            countGreen  = 30
                        }
                    }
                if (msg!!.obj as Boolean){
                    //ส่งข้อมูลให้ Handler Background รอคิวทำงาน
                    var msgBack = Message()
                    msgBack.obj = isLooper
                    backgroundHandler.sendMessage(msgBack)
                }
            }
        }


        //ส่งข้อมูลให้ Handler Background รอคิวทำงาน
        var msgBack = Message()
        msgBack.obj = isLooper
        backgroundHandler.sendMessage(msgBack)

    }

    override fun onDestroy() {
        super.onDestroy()
        backgroundHandlerThread.quit()
    }
}
