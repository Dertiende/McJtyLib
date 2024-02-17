package mcjty.lib.modules;

import mcjty.lib.datagen.DataGen;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public interface IModule {

    void init(FMLCommonSetupEvent event);
    void initClient(FMLClientSetupEvent event);
    void initConfig(IEventBus bus);

    default void initDatagen(DataGen dataGen) {}
}
