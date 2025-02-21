%====================================================================================
% scale description   
%====================================================================================
dispatch( load_weight, load_weight(WEIGHT) ).
dispatch( unload_weight, unload_weight(WEIGHT) ).
dispatch( update_scale_count, update_scale_count(COUNT) ).
event( scale_data, scale_data(WEIGHT) ).
%====================================================================================
context(ctxwis24, "127.0.0.1",  "TCP", "8121").
context(ctxscale, "localhost",  "TCP", "8123").
 qactor( wis, ctxwis24, "external").
  qactor( scale, ctxscale, "it.unibo.scale.Scale").
 static(scale).
  qactor( scale_device, ctxscale, "it.unibo.scale_device.Scale_device").
 static(scale_device).
  qactor( mock_rp_loader_external, ctxscale, "it.unibo.mock_rp_loader_external.Mock_rp_loader_external").
 static(mock_rp_loader_external).
