package com.nureddinelmas.onlinesales.di


import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.nureddinelmas.onlinesales.repository.Repository
import com.nureddinelmas.onlinesales.repository.RepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped


@Module
@InstallIn(ActivityRetainedComponent::class)
class AppModule {
	@Provides
	@ActivityRetainedScoped
	fun provideFirebaseFirestore(): FirebaseFirestore {
		return Firebase.firestore
	}
	
	@Provides
	@ActivityRetainedScoped
	fun provideRepository(
		firestore: FirebaseFirestore
	): Repository = RepositoryImpl(firestore)
}