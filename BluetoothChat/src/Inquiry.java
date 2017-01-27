import java.util.ArrayList;
import java.util.List;

import javax.bluetooth.*;

// Versi�n modificada del inquiry actual para este prop�sito

// Esta clase proporciona funcionalidad util empleada por la clase ServiceFinder para el descubrimiento de servicios

public class Inquiry implements DiscoveryListener {
	private static final int SERVICE_NAME_ATTRID = 0x0100;
	private List<ServiceRecord> serviceList; //This list will contain all the services to be discovered
	private List<RemoteDevice> remoteDeviceList;
	private LocalDevice localDevice;
	private DiscoveryAgent discoveryAgent;
	private int counter;
	
	public Inquiry(){
		try {
			counter = 1;
			serviceList = new ArrayList<>();
			remoteDeviceList = new ArrayList<>();
			localDevice = LocalDevice.getLocalDevice();
			discoveryAgent = localDevice.getDiscoveryAgent();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}
	
	// Este metodo se ejecuta cada vez que se descubre un dispositivo
	public void deviceDiscovered(RemoteDevice remoteDevice, DeviceClass deviceClass) {
		String name = "";
		try {
			name = remoteDevice.getFriendlyName(true);
		} catch (java.io.IOException e) {
			e.printStackTrace();
		}
		System.out.println(this.counter+". "+ name);
		remoteDeviceList.add(remoteDevice);
		this.counter++;
		
		// Basicamente se a�ade a una lista y se presenta en pantalla su friendly name acompa�ado de un numero que le va a permitir al usuario
		// seleccionar el dispositivo sobre el que quiere hacer la busqueda de servicios mas adelante
	}

	// Ejecutado al finalizar la busqueda de dispositivos
	public void inquiryCompleted(int discType) {
		synchronized (this) {
			try {
				System.out.println("Device search completed");
				this.notifyAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	// Ejecutado al finalizar la b�squeda de servicios
	public void serviceSearchCompleted(int tid, int rid) {
		synchronized (this) {
			try {
				this.notifyAll();
			} catch (Exception e) {
			}
		}
		System.out.println("Service Search Completed");
	}

	// Se ejecuta cuando se acaba la busqueda de servicios y se obtiene un array de ServiceRecord
	public void servicesDiscovered(int tid, ServiceRecord[] serviceRecord) {
		if (serviceRecord.length == 0) {
			System.out.println("Device has no service");
		}

		for (int i = 0; i < serviceRecord.length; i++) {
			DataElement d = serviceRecord[i].getAttributeValue(SERVICE_NAME_ATTRID);
			if (d != null) {
				// System.out.println((String) d.getValue());
				serviceList.add(serviceRecord[i]);
			}
			// Solo se a�adiran a la lista servicios que tengan un nombre
		}
	}
	
	public void showLocalDeviceInformation(){
		System.out.println("Local Device Information:");
		System.out.println("Name: " + localDevice.getFriendlyName());
		System.out.println("Bluetooth Address: " + localDevice.getBluetoothAddress());
	}
	
	// Busqeuda de dispositivos
	public void searchDevices(){
		try {
			System.out.println("Searching for devices...");
			discoveryAgent.startInquiry(DiscoveryAgent.GIAC,this); // Busqueda con codigo de acceso general: Visible para todos
			synchronized (this) {
				try {
					this.wait();
				} catch (Exception e) {
				}
			}
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
	}
	
	// Permite llevar a cabo la busqueda de servicios cuando se le da un numero de dispositivo
	// que se encuentra en la lista de dispositivos descubiertos
	// Por tanto un requisito antes de ejecutar esta funcion es haber ejecutado searchDevices
	public List<ServiceRecord> searchServices(int deviceNumber){
		try{
			
			UUID uuids[] = new UUID[1];
			uuids[0] = new UUID(0x1002);
			int attribset[] = new int[1];
			attribset[0] = SERVICE_NAME_ATTRID;
			
			
					// El this se debe a que esta clase es en si el listener
					discoveryAgent.searchServices(attribset, uuids, remoteDeviceList.get(deviceNumber), this);
				synchronized (this) {
					try {
						this.wait();
					} catch (Exception e) {
					}
				};
			
		}catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		
		return serviceList; // Se devuelve una lista de ServiceRecord
	}
	
	
	// Getter para serviceList
	public List<ServiceRecord> getServiceList() {
		return serviceList;
	}
	
}
