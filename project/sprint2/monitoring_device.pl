%====================================================================================
% monitoring_device description   
%====================================================================================
%====================================================================================
context(ctxmonitoringdevice, "localhost",  "TCP", "8125").
 qactor( led, ctxmonitoringdevice, "it.unibo.led.Led").
 static(led).
  qactor( sonar, ctxmonitoringdevice, "it.unibo.sonar.Sonar").
 static(sonar).
