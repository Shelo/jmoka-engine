package com.moka.components;

import com.moka.graphics.DrawableComponent;
import com.moka.graphics.Renderer;
import com.moka.resources.utils.EntityBuffer;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Entity;

public class TileMap extends DrawableComponent
{
    private EntityBuffer entities;

    private byte[] tiles;

    private boolean updateProcess = false;

    private int cellHeight;
    private int cellWidth;

    private int height;
    private int width;

    @Override
    public void onCreate()
    {
        tiles = new byte[width * height];

        for (int i = 0; i < tiles.length; i++) {
            tiles[i] = -1;
        }

        for (Entity entity : entities) {
            entity.getTransform().setSize(cellWidth, cellHeight);
            entity.create();
            Sprite sprite = (Sprite) entity.getDrawable();
            sprite.setBatch(true);
        }
    }

    @Override
    public void onUpdate()
    {
        if (!updateProcess) {
            return;
        }

        for (Entity entity : entities) {
            entity.update();
        }
    }

    @Override
    public void render(Renderer renderer)
    {
        float offsetX = getTransform().getPosition().x;
        float offsetY = getTransform().getPosition().y;

        for (int i = 0; i < tiles.length; i++) {
            if (tiles[i] != -1) {
                int x = i % width;
                int y = i / width;

                Entity entity = entities.get(tiles[i]);
                if (entity.hasDrawable() && entity.getDrawable().isEnabled()) {
                    entity.getTransform().setPosition(x * cellWidth + offsetX,
                            y * cellHeight + offsetY);
                    entity.getDrawable().render(renderer);
                }
            }
        }
    }

    @Override
    public boolean shouldBatch()
    {
        return true;
    }

    public void line(int x, int y, int width, int height, byte tile)
    {
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                setTile(i, j, tile);
            }
        }
    }

    public void setTile(int x, int y, byte tile)
    {
        if (tile >= entities.size() || tile < 0) {
            raiseError("No such tile: " + 0);
        }

        tiles[x + y * width] = tile;
    }

    /**
     * @param width the width of the cell.
     * @param height the height of the cell.
     */
    @ComponentAttribute("CellSize")
    public void setCellWidth(int width, int height)
    {
        cellWidth = width;
        cellHeight = height;
    }

    /**
     * @param width numbers of columns.
     * @param height number of rows.
     */
    @ComponentAttribute("Size")
    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    /**
     * @param entities entities from which the tiles will be taken.
     */
    @ComponentAttribute(value = "Tiles", required = true)
    public void setTiles(EntityBuffer entities)
    {
        this.entities = entities;
    }

    /**
     * @param updateProcess whether the tiles should go through the update process.
     */
    @ComponentAttribute(value = "UpdateProcess")
    public void setUpdateProcess(boolean updateProcess)
    {
        this.updateProcess = updateProcess;
    }
}
