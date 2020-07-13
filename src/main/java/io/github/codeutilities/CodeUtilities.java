package io.github.codeutilities;

import io.github.codeutilities.config.ModConfig;
import io.github.codeutilities.util.ChatType;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.serializer.DummyConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.LiteralText;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

public class CodeUtilities implements ModInitializer {

   public static final String MOD_ID = "codeutilities";
   public static final String MOD_NAME = "CodeUtilities";
   public static final String SONG_PARSER_VERSION = "4"; //NBS parser version
   public static final String SONG_NBS_FORMAT_VERSION = "4"; //NBS format version
   public static Logger LOGGER = LogManager.getLogger();
   public static MinecraftClient mc = MinecraftClient.getInstance();
   public static Random rng = new Random();

   public static void openGuiAsync(LightweightGuiDescription gui) {
      new Thread(() -> {
         try {
            Thread.sleep(0);
         } catch (InterruptedException e) {
            e.printStackTrace();
         }
         openGuiSync(gui);
      }).start();
   }

   public static void openGuiSync(LightweightGuiDescription gui) {
      MinecraftClient.getInstance().openScreen(new CottonClientScreen(gui));
   }

   public static void giveCreativeItem(ItemStack item) {
	   assert MinecraftClient.getInstance().player != null;
	    for (int index = 0; index < MinecraftClient.getInstance().player.inventory.main.size(); index++) {
	      ItemStack i = MinecraftClient.getInstance().player.inventory.main.get(index);
	      ItemStack compareItem = i.copy();
	      compareItem.setCount(item.getCount());
	      if (item == compareItem) {
	        while (i.getCount() < i.getMaxCount() && item.getCount() > 0) {
	          i.setCount(i.getCount() + 1);
	          item.setCount(item.getCount() - 1);
	        }
	      } else {
	        if (i.getItem() == Items.AIR) {
	          assert MinecraftClient.getInstance().interactionManager != null;
	          if (index < 9) MinecraftClient.getInstance().interactionManager.clickCreativeStack(item, index + 36);
	          MinecraftClient.getInstance().player.inventory.main.set(index, item);
	          return;
	        }
	      }
	    }
   }

   public static void log(Level level, String message) {
      LOGGER.log(level, "[" + MOD_NAME + "] " + message);
   }

   public static void chat(String text) {
       if (mc.player != null) {
           mc.player.sendMessage(new LiteralText(text), false);
       }
   }

   public static void chat(String text, ChatType prefixType) {
        if (mc.player != null) {
            mc.player.sendMessage(new LiteralText(prefixType.getString() + text), false);
        }
   }

   @Override
   public void onInitialize() {
      log(Level.INFO, "Initializing");

      AutoConfig.register(ModConfig.class, DummyConfigSerializer::new);
   }

}