package net.techtastic.vc.block

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Rotation
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.VoxelShape
import net.techtastic.vc.util.DirectionalShape
import net.techtastic.vc.util.RotShapes

class MotorTopBlock(properties: Properties) : Block(properties) {
    val MOTOR_TOP = RotShapes.or(
            RotShapes.box(0.0, 8.0, 0.0, 16.0, 16.0, 16.0),
            RotShapes.box(6.0, 4.0, 6.0, 10.0, 8.0, 10.0)
    )

    val MOTOR_TOP_SHAPE = DirectionalShape.up(MOTOR_TOP)

    init {
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.FACING, Direction.NORTH))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(BlockStateProperties.FACING)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState()
                .setValue(BlockStateProperties.FACING, ctx.clickedFace)
    }

    override fun getShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return MOTOR_TOP_SHAPE[state.getValue(BlockStateProperties.FACING)]
    }

    override fun getCollisionShape(state: BlockState, level: BlockGetter, pos: BlockPos, context: CollisionContext): VoxelShape {
        return MOTOR_TOP_SHAPE[state.getValue(BlockStateProperties.FACING)]
    }
}