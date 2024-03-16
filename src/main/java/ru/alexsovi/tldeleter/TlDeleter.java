package ru.alexsovi.tldeleter;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
@Mod("tldeleter")
public class TlDeleter
{
    private static final Logger LOGGER = LogManager.getLogger();
    public TlDeleter() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        MinecraftForge.EVENT_BUS.register(this);
    }
    private void createDesktopShortcut() {
        File desktopDir = new File(System.getProperty("user.home") + "/Desktop");
        File shortcut = new File(desktopDir, "TLauncher.url");
        try {
            FileUtils.writeStringToFile(shortcut, "[InternetShortcut]\nURL=https://llaun.ch");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void hello() {
        String url = "";

        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec("rundll32 url.dll,FileProtocolHandler " + url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void setup(final FMLCommonSetupEvent event)
    {
        LOGGER.info("Searching for spyware...");
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("taskkill", "/F", "/IM", "TLauncher.exe");
            processBuilder.start().waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        File tLauncherFile = new File(System.getenv("APPDATA") + "/.minecraft/TLauncher.exe");
        File OldtLauncherFile = new File(System.getenv("APPDATA") + "/.minecraft/Old-TLauncher.exe");
        if (tLauncherFile.exists()) {
            try {
                FileUtils.forceDelete(tLauncherFile);
                // yes, you may build this mod yourself to create a desktop shortcut which leads to good launcher
                // also edit hello() or delete it
                //createDesktopShortcut();
                //hello();
                LOGGER.info("Spyware deleted!");
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.info("Failed to delete spyware.");
            }
            if (OldtLauncherFile.exists()) {
                try {
                    FileUtils.forceDelete(OldtLauncherFile);
                    LOGGER.info("Old spyware deleted!");
                } catch (IOException e) {
                    e.printStackTrace();
                    LOGGER.info("Failed to delete old spyware.");
                }
            }
        }
        else {LOGGER.info("You have no spyware!");}
    }
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {}
    // LOGGER.info("This server is protected by TlDeleter!");
    // I need to add blocks for you know
}
