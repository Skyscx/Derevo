package events

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.entity.Fireball
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.util.Vector


class FireballEvent : Listener {
    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        if (event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK) {
            if (player.inventory.itemInMainHand.type == Material.FIRE_CHARGE) {
                event.isCancelled = true
                val location: Location = player.eyeLocation
                val direction: Vector = location.direction.normalize()
                val pitch = location.pitch
                if (pitch in -89.0..89.0) {
                    // Центральный
                    val fireball1 = player.world.spawnEntity(location, EntityType.FIREBALL) as Fireball
                    fireball1.direction = direction

                    // Левый
                    val leftDirection: Vector = direction.clone().rotateAroundY(Math.toRadians(-25.0))
                    val fireball2 = player.world.spawnEntity(location, EntityType.FIREBALL) as Fireball
                    fireball2.direction = leftDirection

                    // Правый
                    val rightDirection: Vector = direction.clone().rotateAroundY(Math.toRadians(25.0))
                    val fireball3 = player.world.spawnEntity(location, EntityType.FIREBALL) as Fireball
                    fireball3.direction = rightDirection
                } else {
                    // Центральный
                    val fireball1 = player.world.spawnEntity(location, EntityType.FIREBALL) as Fireball
                    fireball1.direction = direction

                    // Левый
                    val leftLocation = location.clone().add(direction.clone().crossProduct(Vector(0, 1, 0)).normalize().multiply(-1))
                    val leftDirection: Vector = direction.clone().rotateAroundY(Math.toRadians(-25.0))
                    val fireball2 = player.world.spawnEntity(leftLocation, EntityType.FIREBALL) as Fireball
                    fireball2.direction = leftDirection

                    // Правый
                    val rightLocation = location.clone().add(direction.clone().crossProduct(Vector(0, 1, 0)).normalize().multiply(1))
                    val rightDirection: Vector = direction.clone().rotateAroundY(Math.toRadians(25.0))
                    val fireball3 = player.world.spawnEntity(rightLocation, EntityType.FIREBALL) as Fireball
                    fireball3.direction = rightDirection
                }
            }
        }
    }
}