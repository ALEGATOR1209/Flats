package com.alegator1209

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alegator1209.datasource.remote.RemoteDataSource
import com.alegator1209.datasource.remote.api.ApiClient
import com.alegator1209.interactors.FlatRoomsUseCases
import com.alegator1209.interactors.LoginUseCase
import com.alegator1209.ui.flats.FlatsFragment
import com.alegator1209.ui.login.LoginFragment
import com.alegator1209.ui.rooms.RoomsFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, LoginFragment.newInstance(this::onLogin))
                .commitNow()
        }
    }

    private fun onLogin(loginUseCase: LoginUseCase) {
        val dataSource = RemoteDataSource(ApiClient().getApiService(), loginUseCase)
        supportFragmentManager.beginTransaction()
//            .setCustomAnimations(
//                R.anim.enter_from_bottom,
//                R.anim.exit_to_top,
//                R.anim.enter_from_bottom,
//                R.anim.exit_to_top
//            )
            .replace(R.id.container, FlatsFragment.newInstance(dataSource, this::onFlatChosen))
            .disallowAddToBackStack()
            .commitNow()
    }

    private fun onFlatChosen(useCases: FlatRoomsUseCases) {
        supportFragmentManager.beginTransaction()
//            .setCustomAnimations(
//                R.anim.enter_from_bottom,
//                R.anim.exit_to_top,
//                R.anim.enter_from_bottom,
//                R.anim.exit_to_top
//            )
            .replace(R.id.container, RoomsFragment.newInstance(useCases))
            .addToBackStack(useCases.flat.name)
            .commit()
    }
}
