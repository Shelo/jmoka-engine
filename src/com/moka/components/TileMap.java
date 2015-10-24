package com.moka.components;

import com.moka.graphics.Drawable;
import com.moka.graphics.Shader;
import com.moka.graphics.SpriteBatch;
import com.moka.graphics.Texture;
import com.moka.resources.utils.EntityBuffer;
import com.moka.scene.entity.ComponentAttribute;
import com.moka.scene.entity.Entity;

public class TileMap extends Drawable
{
    private EntityBuffer entities;

    private byte[] tiles;

    private int cellHeight;
    private int cellWidth;

    private int height;
    private int width;

    private SpriteBatch batch;

    /**
     * @deprecated
     */
    public void init()
    {
        tiles = new byte[width * height];

        for (int i = 0; i < tiles.length; i++)
            tiles[i] = - 1;
    }

    @Override
    public void onCreate()
    {

    }

    @Override
    public void render(Shader shader)
    {
        float offsetX = getTransform().getPosition().x;
        float offsetY = getTransform().getPosition().y;

        for (int i = 0; i < tiles.length; i++)
        {
            if (tiles[i] != -1)
            {
                int x = i % width;
                int y = i / width;

                Entity entity = entities.get(tiles[i]);
                if (entity.hasDrawable())
                {
                    entity.getTransform().setPosition(x * cellWidth + offsetX,
                            y * cellHeight + offsetY);
                    entity.getTransform().setSize(cellWidth, cellHeight);
                    entity.getDrawable().render(shader);
                }
            }
        }
    }

    public void line(int x, int y, int width, int height, byte tile)
    {
        for (int i = x; i < x + width; i++)
        {
            for (int j = y; j < y + height; j++)
                setTile(i, j, tile);
        }
    }

    public void setTile(int x, int y, byte tile)
    {
        tiles[x + y * width] = tile;
    }

    @ComponentAttribute("CellSize")
    public void setCellWidth(int width, int height)
    {
        cellWidth = width;
        cellHeight = height;
    }

    @ComponentAttribute("Size")
    public void setSize(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    @ComponentAttribute(value = "Tiles", required = true)
    public void setTiles(EntityBuffer entities)
    {
        this.entities = entities;
    }
}
