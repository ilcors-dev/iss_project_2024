%====================================================================================
% monitoringdevice description   
%====================================================================================
dispatch( update_led_mode, update_led_mode(MODE) ).
dispatch( update_physical_led_mode, update_physical_led_mode(MODE) ).
event( sonar_data, distance(D) ).
dispatch( sonar_sensitivity, sonar_sensitivity(S) ).
dispatch( ash_measurement, ash_measurement(L) ).
dispatch( load_ash, load_ash(D) ).
dispatch( unload_ash, unload_ash(D) ).
event( mqtt_info, mqtt_info(MSG) ).
%====================================================================================
context(ctxwis24, "127.0.0.1",  "TCP", "8121").
context(ctxmonitoringdevice, "localhost",  "TCP", "8122").
 qactor( wis, ctxwis24, "external").
  qactor( led, ctxmonitoringdevice, "it.unibo.led.Led").
 static(led).
  qactor( led_device, ctxmonitoringdevice, "it.unibo.led_device.Led_device").
 static(led_device).
  qactor( sonar, ctxmonitoringdevice, "it.unibo.sonar.Sonar").
 static(sonar).
  qactor( sonar_device, ctxmonitoringdevice, "it.unibo.sonar_device.Sonar_device").
 static(sonar_device).
  qactor( mock_ash_unloader_external, ctxmonitoringdevice, "it.unibo.mock_ash_unloader_external.Mock_ash_unloader_external").
 static(mock_ash_unloader_external).
