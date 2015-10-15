/*
 * This file is part of Michael Vorburger's SwissKnightMinecraft project, licensed under the MIT License (MIT).
 *
 * Copyright (c) Michael Vorburger <http://www.vorburger.ch>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ch.vorburger.minecraft.testsinfra;

import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.google.inject.Inject;

/**
 * Minecraft Sponge powered Integration Tests Runner Plugin.
 *
 * @author Michael Vorburger
 */
@Plugin(id = "TestsRunner", name = "Integration Tests Runner", version = "1.0")
public class TestsRunnerPlugin {

	// These two private static fields are read by (TODO reference final helper class/method name)
	@SuppressWarnings("unused") private static boolean isServerStarted;
	@SuppressWarnings("unused") private static Game _game;

	protected @Inject Logger logger;
	protected @Inject Game game;

	@Listener
	public final void onServerStarting(GameStartedServerEvent event) {
		isServerStarted = true;
		_game = game;
		// logger.info("onServerStarting(GameStartedServerEvent): isServerStarted = true [{}]", TestsRunnerPlugin.class.getClassLoader());
	}

	@Listener
	public final void onServerStarting(GameStoppedServerEvent event) {
		isServerStarted = false;
	}

}
