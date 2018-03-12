package com.xiao1zhao2.myjavaagent.agent;

import org.junit.Test;

import java.util.Random;

public class AgentTest {

	private Random random = new Random();

	private void a() throws Exception {
		Thread.sleep(random.nextInt(100));
		a1();
		a2();
		a3();
	}

	private void a1() throws Exception {
		Thread.sleep(random.nextInt(100));
	}

	private void a2() throws Exception {
		Thread.sleep(random.nextInt(100));
	}

	private void a3() throws Exception {
		Thread.sleep(random.nextInt(100));
	}

	private void b() throws Exception {
		Thread.sleep(random.nextInt(100));
		b1();
		b2();
	}

	private void b1() throws Exception {
		Thread.sleep(random.nextInt(100));
	}

	private void b2() throws Exception {
		Thread.sleep(random.nextInt(100));
	}

	private void c() throws Exception {
		Thread.sleep(random.nextInt(100));
		c1();
	}

	private void c1() throws Exception {
		Thread.sleep(random.nextInt(100));
	}

	@Test
	public void testAgent() throws Exception {
		Thread.sleep(random.nextInt(100));
		a();
		b();
		c();
	}
}
