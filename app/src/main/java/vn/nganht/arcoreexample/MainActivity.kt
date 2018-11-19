package vn.nganht.arcoreexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.ar.sceneform.ux.ArFragment


class MainActivity : AppCompatActivity() {
    private lateinit var fragment: ArFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragment = supportFragmentManager.findFragmentById(R.id.sceneformFragment) as ArFragment
    }
}
