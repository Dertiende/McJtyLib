package mcjty.lib.multipart;

public class MultipartBlock {}/* extends Block implements WailaInfoProvider, TOPInfoProvider, EntityBlock {

    public static final AABB AABB_EMPTY = new AABB(0, 0, 0, 0, 0, 0);
    public static final AABB AABB_CENTER = new AABB(.4, .4, .4, .6, .6, .6);

    public MultipartBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL)
                .strength(2.0f)
                .noOcclusion()
                .sound(SoundType.METAL));
        setRegistryName(McJtyLib.MODID, "multipart");
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new MultipartTE(pos, state);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        MultipartTE.Part part = getHitPart(state, world, pos, player.getEyePosition(0), target.getLocation());
        if (part != null) {
            return new ItemStack(part.getState().getBlock().asItem());
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Nonnull
    @Override
    @Deprecated
    public ItemStack getCloneItemStack(@Nonnull BlockGetter worldIn, @Nonnull BlockPos pos, @Nonnull BlockState state) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return combinePartShapes(world, pos, s -> s.getShape(world, pos, context));
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos, @Nonnull CollisionContext context) {
        return combinePartShapes(world, pos, s -> s.getCollisionShape(world, pos, context));
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getInteractionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return combinePartShapes(world, pos, s -> s.getVisualShape(world, pos, CollisionContext.empty()));
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull BlockGetter world, @Nonnull BlockPos pos) {
        return combinePartShapes(world, pos, s -> s.getOcclusionShape(world, pos));
    }

    private VoxelShape combinePartShapes(BlockGetter world, BlockPos pos, Function<BlockState, VoxelShape> shapeGetter) {
        VoxelShape combined = Shapes.empty();
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof MultipartTE) {
            MultipartTE multipartTE = (MultipartTE) te;
            for (Map.Entry<PartSlot, MultipartTE.Part> entry : multipartTE.getParts().entrySet()) {
                MultipartTE.Part part = entry.getValue();
                VoxelShape shape = shapeGetter.apply(part.getState());
                if (combined.isEmpty()) {
                    combined = shape;
                } else {
                    combined = Shapes.join(combined, shape, BooleanOp.OR);
                }
            }
        }
        return combined;
    }

    @Nonnull
    @Override
    @Deprecated
    public InteractionResult use(@Nonnull BlockState state, @Nonnull Level world, BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand, BlockHitResult result) {
        Vec3 hit = result.getLocation();
        Vec3 start = MultipartHelper.getPlayerEyes(player);
        Vec3 end = new Vec3(start.x + (pos.getX() + hit.x - start.x) * 3, start.y + (pos.getY() + hit.y - start.y) * 3, start.z + (pos.getZ() + hit.z - start.z) * 3);
        MultipartTE.Part part = getHitPart(state, world, pos, start, end);
        if (part != null) {
            if (part.getTileEntity() instanceof GenericTileEntity genTileEntity) {
                return genTileEntity.onBlockActivated(part.getState(), player, hand, result);
            } else {
                return part.getState().getBlock().use(part.getState(), world, pos, player, hand, result);
            }
        }
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof MultipartTE multipartTE) {
            multipartTE.dump();
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    public BlockState getHitState(BlockState blockState, Level world, BlockPos pos, Vec3 start, Vec3 end) {
        MultipartTE.Part part = getHitPart(blockState, world, pos, start, end);
        if (part != null) {
            return part.getState();
        } else {
            return null;
        }
    }

    @Nullable
    public static MultipartTE.Part getHitPart(BlockState blockState, BlockGetter world, BlockPos pos, Vec3 start, Vec3 end) {
        BlockEntity te = world.getBlockEntity(pos);
        if (te instanceof MultipartTE multipartTE) {
            for (Map.Entry<PartSlot, MultipartTE.Part> entry : multipartTE.getParts().entrySet()) {
                MultipartTE.Part part = entry.getValue();
                if (!(part.getState().getBlock() instanceof MultipartBlock)) {     // @todo safety
                    // @todo 1.14
                    BlockHitResult result = part.getState().getVisualShape(world, pos, CollisionContext.empty()).clip(start, end, pos);
                    if (result != null) {
                        return part;
                    }
                }
            }
            return null;
        } else {
            return null;
        }
    }

    @Override
    public TOPDriver getProbeDriver() {
        return McJtyLibTOPDriver.DRIVER;
    }


    // @todo 1.14
//    @Override
//    @Optional.Method(modid = "theoneprobe")
//    public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world, BlockState blockState, IProbeHitData data) {
//        MultipartTE.Part part = getHitPart(blockState, world, data.getPos(), MultipartHelper.getPlayerEyes(player), data.getHitVec());
//        if (part != null) {
//            if (part.getTileEntity() instanceof GenericTileEntity) {
//                ((GenericTileEntity) part.getTileEntity()).addProbeInfo(mode, probeInfo, player, world, blockState, data);
//            } else if (part.getState().getBlock() instanceof TOPInfoProvider) {
//                ((TOPInfoProvider) part.getState().getBlock()).addProbeInfo(mode, probeInfo, player, world, blockState, data);
//            }
//        }
//    }
//
//    @Override
//    @Optional.Method(modid = "waila")
//    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
//        MultipartTE.Part part = getHitPart(accessor.getBlockState(), accessor.getWorld(), accessor.getPosition(),
//                MultipartHelper.getPlayerEyes(accessor.getPlayer()), accessor.getRenderingPosition());
//        if (part != null) {
//            if (part.getTileEntity() instanceof GenericTileEntity) {
//                ((GenericTileEntity) part.getTileEntity()).addWailaBody(itemStack, currenttip, accessor, config);
//            } else if (part.getState().getBlock() instanceof WailaInfoProvider) {
//                return ((WailaInfoProvider) part.getState().getBlock()).getWailaBody(itemStack, currenttip, accessor, config);
//            }
//        }
//        return currenttip;
//    }


    // @todo 1.16
//    @Override
//    public BlockState getExtendedState(BlockState state, IBlockReader world, BlockPos pos) {
//        IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
//
//        TileEntity te = world.getTileEntity(pos);
//        if (te instanceof MultipartTE) {
//            MultipartTE multipartTE = (MultipartTE) te;
//            return extendedBlockState.withProperty(PARTS, multipartTE.getParts());
//        }
//        return extendedBlockState;
        // @todo 1.14
//        return state;
//    }
}
*/