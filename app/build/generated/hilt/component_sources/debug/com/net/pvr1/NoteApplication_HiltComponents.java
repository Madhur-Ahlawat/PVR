package com.net.pvr1;

import com.net.pvr1.di.NetworkModule;
import com.net.pvr1.ui.enableLocation.EnableLocationActivity_GeneratedInjector;
import com.net.pvr1.ui.enableLocation.viewModel.EnableLocationViewModel_HiltModules;
import com.net.pvr1.ui.giftCard.GiftCardActivity_GeneratedInjector;
import com.net.pvr1.ui.giftCard.viewModel.giftCardViewModel_HiltModules;
import com.net.pvr1.ui.home.HomeActivity_GeneratedInjector;
import com.net.pvr1.ui.home.fragment.cinema.viewModel.CinemaViewModel_HiltModules;
import com.net.pvr1.ui.home.fragment.commingSoon.viewModel.ComingSoonViewModel_HiltModules;
import com.net.pvr1.ui.home.fragment.home.viewModel.HomeViewModel_HiltModules;
import com.net.pvr1.ui.login.LoginActivity_GeneratedInjector;
import com.net.pvr1.ui.login.viewModel.LoginViewModel_HiltModules;
import com.net.pvr1.ui.movieDetails.MovieDetailsActivity_GeneratedInjector;
import com.net.pvr1.ui.movieDetails.viewModel.MovieDetailsViewModel_HiltModules;
import com.net.pvr1.ui.myBookings.MyBookingsActivity_GeneratedInjector;
import com.net.pvr1.ui.myBookings.viewModel.MyBookingViewModel_HiltModules;
import com.net.pvr1.ui.offer.OfferActivity_GeneratedInjector;
import com.net.pvr1.ui.offer.viewModel.OfferViewModel_HiltModules;
import com.net.pvr1.ui.onBoarding.LandingActivity_GeneratedInjector;
import com.net.pvr1.ui.otpVerify.OtpVerifyActivity_GeneratedInjector;
import com.net.pvr1.ui.otpVerify.viewModel.OtpVerifyViewModel_HiltModules;
import com.net.pvr1.ui.player.PlayerActivity_GeneratedInjector;
import com.net.pvr1.ui.player.viewModel.PlayerViewModel_HiltModules;
import com.net.pvr1.ui.privateScreenings.PrivateScreeningsActivity_GeneratedInjector;
import com.net.pvr1.ui.privateScreenings.viewModel.PrivateScreenViewModel_HiltModules;
import com.net.pvr1.ui.search.searchCinema.SearchCinemaActivity_GeneratedInjector;
import com.net.pvr1.ui.search.searchCinema.viewModel.CinemaSearchViewModel_HiltModules;
import com.net.pvr1.ui.search.searchComingSoon.SearchComingSoonActivity_GeneratedInjector;
import com.net.pvr1.ui.search.searchComingSoon.viewModel.ComingSoonSearchViewModel_HiltModules;
import com.net.pvr1.ui.search.searchHome.SearchHomeActivity_GeneratedInjector;
import com.net.pvr1.ui.search.searchHome.viewModel.HomeSearchViewModel_HiltModules;
import com.net.pvr1.ui.selectCity.SelectCityActivity_GeneratedInjector;
import com.net.pvr1.ui.selectCity.viewModel.SelectCityViewModel_HiltModules;
import com.net.pvr1.ui.splash.SplashActivity_GeneratedInjector;
import com.net.pvr1.ui.webView.WebViewActivity_GeneratedInjector;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.components.ActivityRetainedComponent;
import dagger.hilt.android.components.FragmentComponent;
import dagger.hilt.android.components.ServiceComponent;
import dagger.hilt.android.components.ViewComponent;
import dagger.hilt.android.components.ViewModelComponent;
import dagger.hilt.android.components.ViewWithFragmentComponent;
import dagger.hilt.android.flags.FragmentGetContextFix;
import dagger.hilt.android.flags.HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.HiltViewModelFactory;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_DefaultViewModelFactories_ActivityModule;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint;
import dagger.hilt.android.internal.lifecycle.HiltWrapper_HiltViewModelFactory_ViewModelModule;
import dagger.hilt.android.internal.managers.ActivityComponentManager;
import dagger.hilt.android.internal.managers.FragmentComponentManager;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint;
import dagger.hilt.android.internal.managers.HiltWrapper_ActivityRetainedComponentManager_LifecycleModule;
import dagger.hilt.android.internal.managers.ServiceComponentManager;
import dagger.hilt.android.internal.managers.ViewComponentManager;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.HiltWrapper_ActivityModule;
import dagger.hilt.android.scopes.ActivityRetainedScoped;
import dagger.hilt.android.scopes.ActivityScoped;
import dagger.hilt.android.scopes.FragmentScoped;
import dagger.hilt.android.scopes.ServiceScoped;
import dagger.hilt.android.scopes.ViewModelScoped;
import dagger.hilt.android.scopes.ViewScoped;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.internal.GeneratedComponent;
import dagger.hilt.migration.DisableInstallInCheck;
import javax.inject.Singleton;

public final class NoteApplication_HiltComponents {
  private NoteApplication_HiltComponents() {
  }

  @Module(
      subcomponents = ServiceC.class
  )
  @DisableInstallInCheck
  abstract interface ServiceCBuilderModule {
    @Binds
    ServiceComponentBuilder bind(ServiceC.Builder builder);
  }

  @Module(
      subcomponents = ActivityRetainedC.class
  )
  @DisableInstallInCheck
  abstract interface ActivityRetainedCBuilderModule {
    @Binds
    ActivityRetainedComponentBuilder bind(ActivityRetainedC.Builder builder);
  }

  @Module(
      subcomponents = ActivityC.class
  )
  @DisableInstallInCheck
  abstract interface ActivityCBuilderModule {
    @Binds
    ActivityComponentBuilder bind(ActivityC.Builder builder);
  }

  @Module(
      subcomponents = ViewModelC.class
  )
  @DisableInstallInCheck
  abstract interface ViewModelCBuilderModule {
    @Binds
    ViewModelComponentBuilder bind(ViewModelC.Builder builder);
  }

  @Module(
      subcomponents = ViewC.class
  )
  @DisableInstallInCheck
  abstract interface ViewCBuilderModule {
    @Binds
    ViewComponentBuilder bind(ViewC.Builder builder);
  }

  @Module(
      subcomponents = FragmentC.class
  )
  @DisableInstallInCheck
  abstract interface FragmentCBuilderModule {
    @Binds
    FragmentComponentBuilder bind(FragmentC.Builder builder);
  }

  @Module(
      subcomponents = ViewWithFragmentC.class
  )
  @DisableInstallInCheck
  abstract interface ViewWithFragmentCBuilderModule {
    @Binds
    ViewWithFragmentComponentBuilder bind(ViewWithFragmentC.Builder builder);
  }

  @Component(
      modules = {
          ApplicationContextModule.class,
          HiltWrapper_FragmentGetContextFix_FragmentGetContextFixModule.class,
          NetworkModule.class,
          ActivityRetainedCBuilderModule.class,
          ServiceCBuilderModule.class
      }
  )
  @Singleton
  public abstract static class SingletonC implements NoteApplication_GeneratedInjector,
      FragmentGetContextFix.FragmentGetContextFixEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedComponentBuilderEntryPoint,
      ServiceComponentManager.ServiceComponentBuilderEntryPoint,
      SingletonComponent,
      GeneratedComponent {
  }

  @Subcomponent
  @ServiceScoped
  public abstract static class ServiceC implements ServiceComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ServiceComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          CinemaSearchViewModel_HiltModules.KeyModule.class,
          CinemaViewModel_HiltModules.KeyModule.class,
          ComingSoonSearchViewModel_HiltModules.KeyModule.class,
          ComingSoonViewModel_HiltModules.KeyModule.class,
          EnableLocationViewModel_HiltModules.KeyModule.class,
          HiltWrapper_ActivityRetainedComponentManager_LifecycleModule.class,
          HomeSearchViewModel_HiltModules.KeyModule.class,
          HomeViewModel_HiltModules.KeyModule.class,
          com.net.pvr1.ui.home.viewModel.HomeViewModel_HiltModules.KeyModule.class,
          LoginViewModel_HiltModules.KeyModule.class,
          MovieDetailsViewModel_HiltModules.KeyModule.class,
          MyBookingViewModel_HiltModules.KeyModule.class,
          ActivityCBuilderModule.class,
          ViewModelCBuilderModule.class,
          OfferViewModel_HiltModules.KeyModule.class,
          OtpVerifyViewModel_HiltModules.KeyModule.class,
          PlayerViewModel_HiltModules.KeyModule.class,
          PrivateScreenViewModel_HiltModules.KeyModule.class,
          SelectCityViewModel_HiltModules.KeyModule.class,
          giftCardViewModel_HiltModules.KeyModule.class
      }
  )
  @ActivityRetainedScoped
  public abstract static class ActivityRetainedC implements ActivityRetainedComponent,
      ActivityComponentManager.ActivityComponentBuilderEntryPoint,
      HiltWrapper_ActivityRetainedComponentManager_ActivityRetainedLifecycleEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityRetainedComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          HiltWrapper_ActivityModule.class,
          HiltWrapper_DefaultViewModelFactories_ActivityModule.class,
          FragmentCBuilderModule.class,
          ViewCBuilderModule.class
      }
  )
  @ActivityScoped
  public abstract static class ActivityC implements EnableLocationActivity_GeneratedInjector,
      GiftCardActivity_GeneratedInjector,
      HomeActivity_GeneratedInjector,
      LoginActivity_GeneratedInjector,
      MovieDetailsActivity_GeneratedInjector,
      MyBookingsActivity_GeneratedInjector,
      OfferActivity_GeneratedInjector,
      LandingActivity_GeneratedInjector,
      OtpVerifyActivity_GeneratedInjector,
      PlayerActivity_GeneratedInjector,
      PrivateScreeningsActivity_GeneratedInjector,
      SearchCinemaActivity_GeneratedInjector,
      SearchComingSoonActivity_GeneratedInjector,
      SearchHomeActivity_GeneratedInjector,
      SelectCityActivity_GeneratedInjector,
      SplashActivity_GeneratedInjector,
      WebViewActivity_GeneratedInjector,
      ActivityComponent,
      DefaultViewModelFactories.ActivityEntryPoint,
      HiltWrapper_HiltViewModelFactory_ActivityCreatorEntryPoint,
      FragmentComponentManager.FragmentComponentBuilderEntryPoint,
      ViewComponentManager.ViewComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ActivityComponentBuilder {
    }
  }

  @Subcomponent(
      modules = {
          CinemaSearchViewModel_HiltModules.BindsModule.class,
          CinemaViewModel_HiltModules.BindsModule.class,
          ComingSoonSearchViewModel_HiltModules.BindsModule.class,
          ComingSoonViewModel_HiltModules.BindsModule.class,
          EnableLocationViewModel_HiltModules.BindsModule.class,
          HiltWrapper_HiltViewModelFactory_ViewModelModule.class,
          HomeSearchViewModel_HiltModules.BindsModule.class,
          HomeViewModel_HiltModules.BindsModule.class,
          com.net.pvr1.ui.home.viewModel.HomeViewModel_HiltModules.BindsModule.class,
          LoginViewModel_HiltModules.BindsModule.class,
          MovieDetailsViewModel_HiltModules.BindsModule.class,
          MyBookingViewModel_HiltModules.BindsModule.class,
          OfferViewModel_HiltModules.BindsModule.class,
          OtpVerifyViewModel_HiltModules.BindsModule.class,
          PlayerViewModel_HiltModules.BindsModule.class,
          PrivateScreenViewModel_HiltModules.BindsModule.class,
          SelectCityViewModel_HiltModules.BindsModule.class,
          giftCardViewModel_HiltModules.BindsModule.class
      }
  )
  @ViewModelScoped
  public abstract static class ViewModelC implements ViewModelComponent,
      HiltViewModelFactory.ViewModelFactoriesEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewModelComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewC implements ViewComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewComponentBuilder {
    }
  }

  @Subcomponent(
      modules = ViewWithFragmentCBuilderModule.class
  )
  @FragmentScoped
  public abstract static class FragmentC implements FragmentComponent,
      DefaultViewModelFactories.FragmentEntryPoint,
      ViewComponentManager.ViewWithFragmentComponentBuilderEntryPoint,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends FragmentComponentBuilder {
    }
  }

  @Subcomponent
  @ViewScoped
  public abstract static class ViewWithFragmentC implements ViewWithFragmentComponent,
      GeneratedComponent {
    @Subcomponent.Builder
    abstract interface Builder extends ViewWithFragmentComponentBuilder {
    }
  }
}
