package com.xiao1zhao2.myjavaagent.attach;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.junit.Test;

import java.io.File;
import java.util.List;

public class AttachTest {

	@Test
	public void testSubString() throws Exception {
		while (true) {
			String sub = "hello world".substring(0, 5);
			System.out.println("sub : " + sub);
			Thread.sleep(1000);
		}
	}

	@Test
	public void testAttach() throws Exception {

		List<VirtualMachineDescriptor> machineList = VirtualMachine.list();
		for (VirtualMachineDescriptor machine : machineList) {
			if (machine.toString().contains("testSubString")) {
				VirtualMachine vm = VirtualMachine.attach(machine);
				vm.loadAgent(System.getProperty("user.dir") + File.separator + "target" + File.separator + "myjavaagent-1.0.0-SNAPSHOT.jar");
				vm.detach();
				break;
			}
		}
	}

}
