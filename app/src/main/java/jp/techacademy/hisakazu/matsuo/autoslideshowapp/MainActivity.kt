package jp.techacademy.hisakazu.matsuo.autoslideshowapp

import android.Manifest
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.provider.MediaStore
import android.content.ContentUris
import android.os.Handler
import android.widget.ImageView
import android.widget.Toast
import jp.techacademy.hisakazu.matsuo.autoslideshowapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(){

    private val PERMISSIONS_REQUEST_CODE = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Android 6.0以降の場合
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // パーミッションの許可状態を確認する
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                // 許可されている
                Log.d("ANDROID2", "許可されている")
                getContentsInfo()
            } else {
                // 許可されていないので許可ダイアログを表示する
                Log.d("ANDROID2", "許可されていない")
                requestPermissions(
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    PERMISSIONS_REQUEST_CODE
                )
            }
            // Android 5系以下の場合
        } else {
            getContentsInfo()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_CODE ->
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("ANDROID2", "許可された")

                }else{
                    Log.d("ANDROID2", "許可されなかった")
                    Toast.makeText(applicationContext, "終了してください", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private fun getContentsInfo() {
        // 画像の情報を取得する
        val resolver = contentResolver
        val cursor = resolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
            null, // 項目(null = 全項目)
            null, // フィルタ条件(null = フィルタなし)
            null, // フィルタ用パラメータ
            null // ソート (null ソートなし)
        )

        cursor!!.moveToFirst()
            val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
            val id = cursor.getLong(fieldIndex)
            val imageUri =
                ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
            Log.d("ANDROID1", "URI : " + id.toString())
            imageView.setImageURI(imageUri)


        start_button.setOnClickListener {

            if (cursor!!.moveToNext()) {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ANDROID1", "URI : " + id.toString())
                imageView.setImageURI(imageUri)

            } else {
                cursor.moveToFirst()
                val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                val id = cursor.getLong(fieldIndex)
                val imageUri =
                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                Log.d("ANDROID1", "URI : " + id.toString())
                imageView.setImageURI(imageUri)
            }
            }

            back_button.setOnClickListener {

                if (cursor!!.moveToPrevious()) {
                    // indexからIDを取得し、そのIDから画像のURIを取得する
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    Log.d("ANDROID1", "URI : " + id.toString())
                    imageView.setImageURI(imageUri)

                } else {
                    cursor.moveToLast()
                    val fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID)
                    val id = cursor.getLong(fieldIndex)
                    val imageUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                    Log.d("ANDROID1", "URI : " + id.toString())
                    imageView.setImageURI(imageUri)
                }

            }

            var mTimer: Timer?=null
            var mHandler=Handler()


            ms_button.setOnClickListener {
            if (mTimer == null) {
                ms_button.text = "停止"
                start_button.isEnabled = false
                back_button.isEnabled = false
            mTimer = Timer()
            mTimer!!.schedule(object:TimerTask() {
                override fun run() {
                    mHandler.post {
                                start_button.callOnClick()
                              }
                    }
                },2000,2000)
            } else {
                mTimer!!.cancel()

                ms_button.text = "再生"
                start_button.isEnabled = true
                back_button.isEnabled = true

                mTimer!!.cancel()
                mTimer = null
            }
        }
    }
}