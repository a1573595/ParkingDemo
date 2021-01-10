package com.a1573595.parkingdemo.parkingInfo;

import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;

import com.a1573595.parkingdemo.BaseActivity;
import com.a1573595.parkingdemo.R;
import com.a1573595.parkingdemo.databinding.ActivityParkingInfoBinding;
import com.a1573595.parkingdemo.model.data.Love;
import com.a1573595.parkingdemo.model.data.Parking;

import org.jetbrains.annotations.NotNull;

import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.observers.DisposableSingleObserver;

public class ParkingInfoActivity extends BaseActivity<ParkingInfoPresenter> implements ParkingInfoView {
    private ActivityParkingInfoBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityParkingInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        presenter.setID(getIntent().getExtras().getString("id"));
        presenter.readParkData();
        presenter.readLoveData();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public DisposableSingleObserver<Parking> showParkInfo() {
        return new DisposableSingleObserver<Parking>() {
            @Override
            public void onSuccess(@NotNull Parking parking) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(parking.name);
                }

                binding.tvName.setText(parking.name);
                binding.tvAddress.setText(parking.address);
                binding.tvArea.setText(parking.area);
                binding.tvPhone.setText(parking.tel);
                binding.tvInfo.setText(parking.summary);
                binding.tvRate.setText(parking.payex);
                binding.tvBus.setText(String.valueOf(parking.totalbus));
                binding.tvVehicle.setText(String.valueOf(parking.totalcar));
                binding.tvMoto.setText(String.valueOf(parking.totalmotor));
                binding.tvBike.setText(String.valueOf(parking.totalbike));

                presenter.addHistory();

                setListen();
            }

            @Override
            public void onError(@NotNull Throwable e) {
            }
        };
    }

    @Override
    public DisposableSingleObserver<Love> showLove() {
        return new DisposableSingleObserver<Love>() {
            @Override
            public void onSuccess(@NotNull Love love) {
                binding.imgLove.setImageResource(R.drawable.love);
            }

            @Override
            public void onError(@NotNull Throwable e) {
            }
        };
    }

    @Override
    public DisposableCompletableObserver changeLove(boolean isLove) {
        return new DisposableCompletableObserver() {
            @Override
            public void onComplete() {
                binding.imgLove.setImageResource(isLove ? R.drawable.love : R.drawable.unlove);

                ScaleAnimation scaleAnimation = new ScaleAnimation(
                        1.0f, 1.2f, 1.0f, 1.2f,
                        Animation.RELATIVE_TO_SELF, .5f,
                        Animation.RELATIVE_TO_SELF, .5f);
                scaleAnimation.setDuration(300);

                binding.imgLove.startAnimation(scaleAnimation);
            }

            @Override
            public void onError(@NotNull Throwable e) {
            }
        };
    }

    private void setListen() {
        binding.imgLove.setOnClickListener(view -> presenter.writeLove(!binding.imgLove.getDrawable().getConstantState().equals(
                getDrawable(R.drawable.love).getConstantState())));
    }
}