%====================================================================================
% monitoringdevice description   
%====================================================================================
dispatch( update_led_mode, update_led_mode(mode) ).
dispatch( update_physical_led_mode, update_physical_led_mode(mode) ).
event( sonar_data, distance(d) ).
dispatch( sonar_sensitivity, sonar_sensitivity(s) ).
dispatch( ash_measurement, ash_measurement(l) ).
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
