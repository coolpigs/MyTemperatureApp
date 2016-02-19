package com.whb.temp;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class TemperatureActivity extends Activity implements View.OnClickListener {
    public static final boolean IS_DEBUG = false;//Macro for output debug information
    public static final String TAG = "WHB_APP ";//output information tag
    public static final int DAYS = 5;
    public static final float MIN_RANDOM_C_TEMP = -50;//min temperature
    public static final float MAX_RANDOM_C_TEMP = 50;//max temperature

    private String DEGREE;// display unit
    private String CELSIUS;// display unit
    private String FAHRENHEIT;// display unit
    private String SLASH;// display
    private String[] WEEKDAYS;
    private String SENSOR_UNAVAILABLE;//error message
    private SensorManager sensorManager;
    private float currentTemp = Float.NaN; // ambient temperature read from sensor
    private Sensor ambientTempSensor;

    private TextView ambientTempTextView;
    private ListView tempListView;
    private Button convertButton;
    private BaseAdapter adapter;

    private float temps[]; //temperature from mon - fri, random generated
    private float temp[]; // use for conversion ambient temperature
    private boolean isCelsius = true;//
    private List<HashMap<String, String>> list = new ArrayList<>();
    private String txt;//ambient temperature
    private int lastTemp;// if ambient temperature has almost no change, then no need to update the textview
    private boolean isClicked = false;//button click

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        init();
        initListView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterSensor();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //release
        list = null;
        temps = null;
        temp = null;
    }

    @Override
    public void onClick(View v) {
        isCelsius = !isCelsius;
        isClicked = true;

        updateTextViewUI();
        updateListViewUI();
        updateButtonUI();
    }


    /**
     * initialize all variable and view
     */
    private void init()
    {
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ambientTempTextView = (TextView) findViewById(R.id.ambientTemperatureTextView);
        tempListView = (ListView) findViewById(R.id.temperatureListView);
        convertButton = (Button) findViewById(R.id.convertButton);

        temp = new float[1];
        DEGREE = this.getResources().getString(R.string.degree);
        CELSIUS = this.getResources().getString(R.string.celsius);
        FAHRENHEIT = this.getResources().getString(R.string.fahrenheit);
        SLASH = this.getResources().getString(R.string.slash);
        WEEKDAYS = this.getResources().getStringArray(R.array.weekdays);
        SENSOR_UNAVAILABLE = this.getResources().getString(R.string.sensor_available);

        convertButton.setText(DEGREE + CELSIUS + SLASH + DEGREE + FAHRENHEIT);
        convertButton.setOnClickListener(this);
    }

    /**
     * initialize listView  which used to display mon - fri temperature
     */
    private void initListView()
    {
        getDefaultTemperature();
        updateListData(CELSIUS);

        adapter = new SimpleAdapter(this,
                list,
                R.layout.list_item,
                new String[]{"weekdays", "temperature"},
                new int[]{R.id.itemWeekdays, R.id.itemTemperature});

        tempListView.setAdapter(adapter);
    }

    private final SensorEventListener ambientTempSensorListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if(event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
                currentTemp = event.values[0];

                if (IS_DEBUG) {
                    System.out.println(TAG + "currentTemp=" + currentTemp);
                }
                updateTextViewUI();
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void registerSensor()
    {
        ambientTempSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        if (ambientTempSensor != null) {
            sensorManager.registerListener(ambientTempSensorListener, ambientTempSensor, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            ambientTempTextView.setText(SENSOR_UNAVAILABLE);
        }
    }

    private void unregisterSensor()
    {
        sensorManager.unregisterListener(ambientTempSensorListener);
    }

    /**
     * update textview for display ambient temperature, the temperature is read
     * from sensor, and if temperature is almost no change with int type, then
     * don't update the textview UI
     */
    private void updateTextViewUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (ambientTempTextView != null && !Float.isNaN(currentTemp) && ((lastTemp != (int)currentTemp) || isClicked)) {
                    lastTemp = (int)currentTemp;
                    isClicked = false;
                    if(!isCelsius)
                    {
                        temp[0] = currentTemp;
                        temp = Conversion.nativeCelsius2Fahrenheit(temp);
                        txt = (int)temp[0] + DEGREE + FAHRENHEIT;
                    }
                    else
                    {
                        txt = (int)currentTemp + DEGREE + CELSIUS;
                    }

                    ambientTempTextView.setText(txt);//usually display int type temperature
                    ambientTempTextView.invalidate();
                }
            }
        });
    }

    /**
     * generate random temperature list range from -50 ~ 50
     */
    private void getDefaultTemperature() {
        Random random = new Random();
        temps = new float[DAYS];
        for (int i = 0; i < DAYS; i++) {
            temps[i] = random.nextFloat() * (MAX_RANDOM_C_TEMP - MIN_RANDOM_C_TEMP) + MIN_RANDOM_C_TEMP;

            if(IS_DEBUG)
            {
                System.out.println(TAG + "temps[" + i + "]=" +temps[i]);
            }
        }

        random = null;
    }

    /**
     * convert temperature from Celsius to Fahrenheit or from Fahrenheit to Celsius using JNI function
     */
    private void updateTemperature()
    {
        if(isCelsius)
        {
            temps = Conversion.nativeFahrenheit2Celsius(temps);
        }
        else
        {
            temps = Conversion.nativeCelsius2Fahrenheit(temps);
        }
    }

    /**
     * temperature list data display in the listview
     * @param type
     */
    private void updateListData(String type)
    {
        list.clear();

        for (int i = 0; i < WEEKDAYS.length; i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("weekdays", WEEKDAYS[i]);
            hashMap.put("temperature", String.valueOf((int) temps[i]) + DEGREE + type);
            list.add(hashMap);
        }
    }

    private void updateListViewUI()
    {
        updateTemperature();
        updateListData(isCelsius ? CELSIUS : FAHRENHEIT);
        adapter.notifyDataSetChanged();
    }

    private void updateButtonUI() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isCelsius == false) {
                    convertButton.setText(DEGREE + FAHRENHEIT + SLASH + DEGREE + CELSIUS);
                } else {
                    convertButton.setText(DEGREE + CELSIUS + SLASH + DEGREE + FAHRENHEIT);
                }
            }
        });
    }

}
