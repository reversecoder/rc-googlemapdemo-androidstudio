# Libraries for android dev

## 1. ProgressDialog
### a) ProgressFragmentDialog
#### Demo:
![ProgressFragmentDialogLogo](https://github.com/reversecoder/rc-librarydemo-androidstudio/blob/master/rc-library/res/drawable-nodpi/ic_progress_fragment_dialog_loading.png)
#### Usage:
```
    //showing progess dialog
    new ProgressDialog().show(getActivity(), "Loading...", "loadingUser");

    //hiding progress dialog
    new ProgressDialog().hide("loadingUser");
```

## 2. AsyncTask
### a) AsyncJob
#### Usage:
i) Sequential dependent tasks
```
    AsyncJob.AsyncAction<TaskResult> loadScreenLayoutData = new AsyncJob.AsyncAction<TaskResult>() {
        @Override
        public TaskResult doOnBackground() {
            AsyncJob.publishProgress(1L, 78L);
            return new TaskResult(true);
        }

        @Override
        public void doOnProgress(Long... progress) {
            if (progress.length == 1) {
                if (progress[0] == 1L) {
                    progressText = "Loading screen layout information, please standby...";
                }
                progressPercentage = progress[0].intValue();
                progressTextView.setText(progressText + " / " + progressPercentage + "%");
            }
        }

        @Override
        public void doWhenFinished(TaskResult taskResult) {
            if (taskResult.isSuccess() && (Boolean) taskResult.getResult()) {
                //do some thing
            }
        }
    };

    ExecutorService executorService = Executors.newSingleThreadExecutor();
    FutureTask ftLoadScreenLayoutData = AsyncJob.doInBackground(loadScreenLayoutData, executorService);
    FutureTask ftProcessPreLoading = AsyncJob.doInBackground(processPreLoading, executorService);
    executorService.shutdown();
```
ii) Standalone background job

iii) Standalone ui thread job

iv) Builder job

### b) AdvancedAsyncTask

## 3. Handler
### a) WeakHandler
#### Usage:
```
    WeakHandler mHandler = new WeakHandler();
    mHandler.postDelayed(new Runnable() {
        //do something
    },5000);
```

## 3. Gauge
### a) BatteryIndicatorGauge
#### Usage:
```
    BatteryIndicatorGauge batteryindicator = (BatteryIndicatorGauge) findViewById(R.id.batteryindicator);
    batteryindicator.setValue(50, 0, 0);
```

### b) SpeedometerGauge
#### Usage:
```
    SpeedometerGauge speedometer = (SpeedometerGauge) findViewById(R.id.speedometer);
    speedometer.setLabelConverter(new SpeedometerGauge.LabelConverter(){
        @Override
        public String getLabelFor ( double progress, double maxProgress){
        return String.valueOf((int) Math.round(progress));
    }
    });
    speedometer.setMaxSpeed(120);
    speedometer.setMajorTickStep(10);
    speedometer.setMinorTicks(4);
    speedometer.addColoredRange(0,80,Color.GREEN);
    speedometer.addColoredRange(80,100,Color.YELLOW);
    speedometer.addColoredRange(100,120,Color.RED);
    speedometer.setSpeed(0,1000,300);
```

## 4. Memory
### a) SessionManager
#### Usage:
```
    SessionManager.setStringSetting(context, "USER_NAME", "Rashed");
    SessionManager.getStringSetting(context, "USER_NAME");
```

## 5. VideoView
### a) AdvancedVideoView
#### Usage:
```
    Uri mSource = Uri.parse(filePath);
    setDataSource(mSource);
    setLooping(true);
    setScalableType(ScalableType.FIT_CENTER);
```

## 6. StateLayout
### a) StateLayout
#### Usage:
i) Basic usage
```
    StateLayout mStateLayout = (StateLayout) findViewById(R.id.sl_layout_state);
    mStateLayout.setStateLayoutView(StateLayout.VIEW_STATE.VIEW_LOADING);
```

ii) Customize usage
```
    mStateLayout.setLoadingMessage("It's loading.");
    mStateLayout.setContentView(R.layout.state_layout_content);
    mStateLayout.setEmptyImage(R.drawable.ic_empty);
```