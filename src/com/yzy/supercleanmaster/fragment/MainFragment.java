package com.yzy.supercleanmaster.fragment;

import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import com.hua.supercleanmaster.listener.FunctionButonClickListener;
import com.umeng.update.UmengUpdateAgent;
import com.yzy.supercleanmaster.R;
import com.yzy.supercleanmaster.base.BaseFragment;
import com.yzy.supercleanmaster.model.SDCardInfo;
import com.yzy.supercleanmaster.ui.AutoStartManageActivity;
import com.yzy.supercleanmaster.ui.MemoryCleanActivity;
import com.yzy.supercleanmaster.ui.RubbishCleanActivity;
import com.yzy.supercleanmaster.ui.SoftwareManageActivity;
import com.yzy.supercleanmaster.utils.AppUtil;
import com.yzy.supercleanmaster.utils.LogUtils;
import com.yzy.supercleanmaster.utils.StorageUtil;
import com.yzy.supercleanmaster.utils.T;
import com.yzy.supercleanmaster.views.HomeButton;
import com.yzy.supercleanmaster.views.ItemCardView;
import com.yzy.supercleanmaster.widget.circleprogress.ArcProgress;


@SuppressLint("NewApi")
public class MainFragment extends BaseFragment implements FunctionButonClickListener{

	/**显示存储空间的View*/
    @InjectView(R.id.arc_store)
    ArcProgress arcStore;

    /**显示内存剩余*/
    @InjectView(R.id.arc_process)
    ArcProgress arcProcess;
    /**显示内存总的大小和剩余的百分比*/
    @InjectView(R.id.capacity)
    TextView capacity;

    @InjectView(R.id.memory_speed)
    HomeButton memory_speed;
//        ItemCardView memory_speed;
    
    //
    @InjectView(R.id.waste_manager)
    HomeButton waste_manager;
//    ItemCardView waste_manager;
    //
    @InjectView(R.id.start_manager)
    HomeButton start_manager;
//    HomeButton start_manager;
//    ItemCardView start_manager;
    //
    @InjectView(R.id.software_manager)
    HomeButton software_manager;
//    ItemCardView software_manager;
    
    Context mContext;
    private static final int  duration =200;

    private Timer timer;
    private Timer timer2;


    @Override
    public View onCreateView(LayoutInflater inflater,
                              ViewGroup container,  Bundle savedInstanceState) {
        // TODO Auto-generated method stub

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        /***这个是注解的是要用*/
        ButterKnife.inject(this, view);
        mContext = getActivity();
        initListener();
        return view;
    }


    private void initListener(){
    	memory_speed.setOnHomeClick(this);
    	waste_manager.setOnHomeClick(this);
    	start_manager.setOnHomeClick(this);
    	software_manager.setOnHomeClick(this);
    }
    
    
    @Override
    public void onResume() {
        super.onResume();
        LogUtils.i("the onResume------");
        fillData();
    }

    @Override
    public void onActivityCreated( Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        UmengUpdateAgent.update(getActivity());
    }

    private void fillData() {
        // TODO Auto-generated method stub
        timer = null;
        timer2 = null;
        timer = new Timer();
        timer2 = new Timer();


        long l = AppUtil.getAvailMemory(mContext);
        long y = AppUtil.getTotalMemory(mContext);
        final double x = (((y - l) / (double) y) * 100);
        //   arcProcess.setProgress((int) x);

        arcProcess.setProgress(0);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcProcess.getProgress() >= (int) x) {
                            timer.cancel();
                        } else {
                            arcProcess.setProgress(arcProcess.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);

        SDCardInfo mSDCardInfo = StorageUtil.getSDCardInfo();
        SDCardInfo mSystemInfo = StorageUtil.getSystemSpaceInfo(mContext);

        long nAvailaBlock;
        long TotalBlocks;
        if (mSDCardInfo != null) {
            nAvailaBlock = mSDCardInfo.free + mSystemInfo.free;
            TotalBlocks = mSDCardInfo.total + mSystemInfo.total;
        } else {
            nAvailaBlock = mSystemInfo.free;
            TotalBlocks = mSystemInfo.total;
        }

        final double percentStore = (((TotalBlocks - nAvailaBlock) / (double) TotalBlocks) * 100);

        capacity.setText(StorageUtil.convertStorage(TotalBlocks - nAvailaBlock) + "/" + StorageUtil.convertStorage(TotalBlocks));
        arcStore.setProgress(0);
//        arcStore.setTextColor(Color.parseColor("#E040FB"));
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {


                        if (arcStore.getProgress() >= (int) percentStore) {
                            timer2.cancel();
                        } else {
                            arcStore.setProgress(arcStore.getProgress() + 1);
                        }

                    }
                });
            }
        }, 50, 20);


    }

//    @OnClick(R.id.memory_speed)
//    void speedUp() {
////        startActivity(MemoryCleanActivity.class);
//    	itemAnimation(memory_speed, MemoryCleanActivity.class);
//    }
//
//
//    @OnClick(R.id.card2)
//    void rubbishClean() {
////        startActivity(RubbishCleanActivity.class);
//    	itemAnimation(waste_manager, RubbishCleanActivity.class);
//    }
//
//
//    @OnClick(R.id.card3)
//    void AutoStartManage() {
////        startActivity(AutoStartManageActivity.class);
//    	itemAnimation(start_manager, AutoStartManageActivity.class);
//    }
//
//    @OnClick(R.id.card4)
//    void SoftwareManage() {
////        startActivity(SoftwareManageActivity.class);
//        itemAnimation(software_manager, SoftwareManageActivity.class);
//    }

    
	public void itemAnimation(final View view,final Class<?> className)
	{

	    PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f,  
                0f, 1f);  
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f,  
                0.8f, 1f);  
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f,  
                0.8f, 1f); 
        
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY,pvhZ);
        animator.setDuration(duration).start();
        animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animator) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animator) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator animator) {
				startActivity(className);
			}
			
			@Override
			public void onAnimationCancel(Animator animator) {
				
			}
		});
        
        
		
	}
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    @Override
    public void onDestroy() {
        timer.cancel();
        timer2.cancel();
        super.onDestroy();
    }


	@Override
	public void onclick(View view) {
		
		int id = view.getId();
		
		switch (id) {
		case R.id.memory_speed:
			startActivity(MemoryCleanActivity.class);
			break;
			
		case R.id.waste_manager:
			startActivity(RubbishCleanActivity.class);
			break;
			
		case R.id.start_manager:
			startActivity(AutoStartManageActivity.class);
			break;
			
		case R.id.software_manager:
			startActivity(SoftwareManageActivity.class);
			break;

		default:
			break;
		}
		
	}
}
