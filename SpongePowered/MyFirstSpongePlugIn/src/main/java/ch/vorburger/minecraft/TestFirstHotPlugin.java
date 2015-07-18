package ch.vorburger.minecraft;

import org.slf4j.Logger;
import org.spongepowered.api.event.GameEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

import ch.vorburger.hotea.minecraft.api.AbstractHotPlugin;

@Plugin(id = "TestFirstHotPlugin", name = "Test first HOT load capable Sponge PlugIn", version = "1.0")
public class TestFirstHotPlugin extends AbstractHotPlugin {

	@Inject Logger logger;

	@Override
	protected void onLoaded(GameEvent event) {
		logger.info("loaded");
	}

	@Override
	protected void onStop(GameEvent event) {
		logger.info("stopped");
	}

}
