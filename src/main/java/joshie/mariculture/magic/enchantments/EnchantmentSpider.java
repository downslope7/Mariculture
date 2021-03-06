package joshie.mariculture.magic.enchantments;

import joshie.mariculture.core.helpers.EnchantHelper;
import joshie.mariculture.core.util.MCTranslate;
import joshie.mariculture.magic.Magic;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.player.EntityPlayer;

public class EnchantmentSpider extends EnchantmentJewelry {
    public static boolean activated = false;
    public static boolean toggledOn = false;
    private static int damageTicker = 0;

    public EnchantmentSpider(int i, int weight, EnumEnchantmentType type) {
        super(i, weight, type);
        setName("spiderman");
        minLevel = 1;
        maxLevel = 25;
    }

    @Override
    public int getMaxLevel() {
        return 1;
    }

    public static void activate(EntityPlayer player) {
        if (activated) if (player.isCollidedHorizontally && toggledOn && !player.isOnLadder()) {
            final float factor = 0.15F;

            if (player.motionX < -factor) {
                player.motionX = -factor;
            }

            if (player.motionX > factor) {
                player.motionX = factor;
            }

            if (player.motionZ < -factor) {
                player.motionZ = -factor;
            }

            if (player.motionZ > factor) {
                player.motionZ = factor;
            }

            player.fallDistance = 0.0F;

            if (player.motionY < -0.14999999999999999D) {
                player.motionY = -0.14999999999999999D;
            }

            player.motionY = 0.20000000000000001D;

            damageTicker++;
            if (damageTicker == 1200) {
                damageTicker = 0;
                EnchantHelper.damageItems(Magic.spider, player, 1);
            }
        }
    }

    public static void set(boolean spider) {
        activated = spider;
    }

    public static String getChat() {
        if (toggledOn) return MCTranslate.translate("enabledSpider");
        else return MCTranslate.translate("disabledSpider");
    }
}
