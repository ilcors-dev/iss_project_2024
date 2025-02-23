%====================================================================================
% monitoringdevice_tests description   
%====================================================================================
dispatch( update_led_mode, update_led_mode(MODE) ).
dispatch( update_physical_led_mode, update_physical_led_mode(MODE) ).
event( sonar_data, distance(D) ).
dispatch( sonar_sensitivity, sonar_sensitivity(S) ).
dispatch( ash_measurement, ash_measurement(L) ).
event( mqtt_info, mqtt_info(MSG) ).
%====================================================================================
context(ctxmonitoringdevice_tests, "localhost",  "TCP", "8122").
 qactor( led, ctxmonitoringdevice_tests, "it.unibo.led.Led").
 static(led).
  qactor( led_device, ctxmonitoringdevice_tests, "it.unibo.led_device.Led_device").
 static(led_device).
  qactor( sonar, ctxmonitoringdevice_tests, "it.unibo.sonar.Sonar").
 static(sonar).
  qactor( sonar_device, ctxmonitoringdevice_tests, "it.unibo.sonar_device.Sonar_device").
 static(sonar_device).
