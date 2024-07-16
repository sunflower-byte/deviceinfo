### 项目概况
设备信息是一个获取android设备信息的测试程序，如属性，基站信息，传感器信息。   
github: git@github.com:sunflower-byte/deviceinfo.git[ ][enter]  

### 快速入门
设备信息包含两部分，deviceinfo目录下的产物是一个aar，app路径下的产物是apk，它调用aar包中的方法采集数据。  
1、属性数据：
DevicePropertyManager 类用来获取属性数据。  
Map<String, String> getProperty() 获取属性数据。  
void exportProperty(String path) 导出属性数据。  
 
2、基站数据获取
DeviceCellinfoManager类用来获取基站信息。  
Map<String, String> getCellInfo() 获取基站信息。  
void exportCellinfo(String path) 导出基站信息到文件中。  

3、传感器数据获取
DeviceSensorManager类用来获取传感器数据。  
boolean register(Sensor sensor, SensorEventListener listener) 注册回调获取某个传感器的数据。  
boolean unregister(Sensor sensor, SensorEventListener listener) 停止获取某个传感器的数据。  
void startCapture(int period, int duration, String path, SensorCaptureListener listener) 采集已经注册的传感器数据，path为保存路径。可以设置采集的间隔和采集时长，当采集完成时，通过SensorCaptureListener通知上层。  
