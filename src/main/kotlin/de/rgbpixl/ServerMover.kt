package de.rgbpixl

import com.google.common.base.Optional
import com.google.common.io.ByteStreams
import net.fabricmc.fabric.api.event.player.AttackBlockCallback
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking.PlayPayloadHandler
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.network.packet.CustomPayload
import net.minecraft.network.packet.s2c.common.CustomPayloadS2CPacket
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.Identifier
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import org.slf4j.Logger
import org.slf4j.LoggerFactory


object ServerMover {

    private val logger: Logger = LoggerFactory.getLogger("chunks_and_money")

    private var huskhomesFabricPluginMessage: Optional<Class<*>> = Optional.absent()

    init {

        logger.info("Loaded ServerMover")



        AttackBlockCallback.EVENT.register(AttackBlockCallback { player: PlayerEntity, world: World, hand: Hand?, pos: BlockPos?, direction: Direction? ->

            logger.info("Player ${player.name} attacked block at $pos")

            if (player is ServerPlayerEntity) {

                val stream = ByteStreams.newDataOutput();

                stream.writeUTF("Connect")
                stream.writeUTF("SUB-ShoppingDistrict")


                if (huskhomesFabricPluginMessage.isPresent) {
                    player.networkHandler.sendPacket(CustomPayloadS2CPacket(huskhomesFabricPluginMessage.get().getConstructor(ByteArray::class.java).newInstance(stream.toByteArray()) as CustomPayload))
                } else {
                    player.networkHandler.sendPacket(CustomPayloadS2CPacket(ChangeServerPayload(stream.toByteArray())))
                }


                logger.info("Sent player ${player.name} to SUB-Farmwelt")

            } else {
                logger.error("Player is not a ServerPlayerEntity")
            }
            ActionResult.PASS
        })

        try {
            huskhomesFabricPluginMessage = Optional.of(Class.forName("net.william278.huskhomes.network.FabricPluginMessage"))

            logger.info("HuskHomes installed, using HuskHomes for server switching")

        } catch (e: ClassNotFoundException) {

            logger.info("Registering ServerMover packets")

            PayloadTypeRegistry.playS2C()
                .register(ChangeServerPayload.CHANNEL_ID, ChangeServerPayload.CODEC)
            PayloadTypeRegistry.playC2S()
                .register(ChangeServerPayload.CHANNEL_ID, ChangeServerPayload.CODEC)

            ServerPlayNetworking.registerGlobalReceiver(ChangeServerPayload.CHANNEL_ID, ChangeServerPayloadHandler())
        }
    }

    class ChangeServerPayload(private val bytes: ByteArray) : CustomPayload {
        companion object {
            fun read(buf: PacketByteBuf): ChangeServerPayload {
                return ChangeServerPayload(getWrittenBytes(buf))
            }

            private fun getWrittenBytes(buf: PacketByteBuf): ByteArray {
                val bs = ByteArray(buf.readableBytes())
                buf.readBytes(bs)
                return bs
            }

            val CHANNEL_ID: CustomPayload.Id<ChangeServerPayload> = CustomPayload.Id(
                Identifier.of("bungeecord", "main")
            )

            val CODEC: PacketCodec<RegistryByteBuf, ChangeServerPayload> =
                PacketCodec.of(
                    { value: ChangeServerPayload, buf: RegistryByteBuf ->
                        writeBytes(
                            buf,
                            value.bytes
                        )
                    },
                    ChangeServerPayload::read
                )


            private fun writeBytes(buf: PacketByteBuf, v: ByteArray) {
                buf.writeBytes(v)
            }
        }


        override fun getId(): CustomPayload.Id<ChangeServerPayload> {
            return CHANNEL_ID
        }
    }

    class ChangeServerPayloadHandler : PlayPayloadHandler<ChangeServerPayload> {
        override fun receive(payload: ChangeServerPayload?, context: ServerPlayNetworking.Context?) {
        }
    }

}
