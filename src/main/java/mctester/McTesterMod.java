package mctester;

import mctester.annotation.TestRegistryHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class McTesterMod implements ModInitializer {
	private static boolean shouldAutorun;
	private static boolean shouldCrashOnFail;
	private static boolean shouldShutdownAfterTest;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		TestRegistryHelper.convertAllNbtToSnbt();

		boolean tmp = TestRegistryHelper.shouldWarnOnMissingStructureFile;
		TestRegistryHelper.shouldWarnOnMissingStructureFile = false;
		TestRegistryHelper.createTestsFromClass(ExampleTests.class);
		TestRegistryHelper.shouldWarnOnMissingStructureFile = tmp;

		TestRegistryHelper.createTemplatedTestsFromFiles();

		shouldAutorun = true;
		shouldCrashOnFail = FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;
		shouldShutdownAfterTest = FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER;


		String autostartProperty = System.getProperty("mctester.autostart");
		if (autostartProperty != null) {
			shouldAutorun = Boolean.parseBoolean(autostartProperty);
		}
		String crashOnFailProperty = System.getProperty("mctester.crashOnFail");
		if (crashOnFailProperty != null) {
			shouldCrashOnFail = Boolean.parseBoolean(crashOnFailProperty);
		}
		String shutdownAfterTestProperty = System.getProperty("mctester.shutdownAfterTest");
		if (shutdownAfterTestProperty != null) {
			shouldShutdownAfterTest = Boolean.parseBoolean(shutdownAfterTestProperty);
		}

	}

	public static boolean shouldAutorun() {
		return shouldAutorun;
	}

	public static boolean shouldCrashOnFail() {
		return shouldCrashOnFail;
	}

	public static boolean shouldShutdownAfterTest() {
		return shouldShutdownAfterTest;
	}
}
