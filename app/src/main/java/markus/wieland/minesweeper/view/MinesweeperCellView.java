package markus.wieland.minesweeper.view;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import markus.wieland.games.elements.Coordinate;
import markus.wieland.games.game.grid.GridGameBoardFieldView;
import markus.wieland.games.game.view.GameStateField;
import markus.wieland.minesweeper.persistence.MinesweeperGameStateField;

public class MinesweeperCellView extends View implements GridGameBoardFieldView {

    private int value;

    private boolean isUncovered;
    private boolean isMarkedAsSave;
    private boolean wasRevealed;

    private Coordinate coordinate;

    public static final int BOMB = -10;

    public MinesweeperCellView(Context context, Coordinate coordinate) {
        super(context);
        this.coordinate = coordinate;
        this.wasRevealed = false;
    }

    public MinesweeperCellView(Context context) {
        super(context);
        this.wasRevealed = false;
    }

    public MinesweeperCellView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.wasRevealed = false;
    }

    public MinesweeperCellView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.wasRevealed = false;
    }

    public boolean isBomb() {
        return value == BOMB;
    }

    @Override
    public void load(GameStateField stateField) {
        MinesweeperGameStateField minesweeperGameStateField = (MinesweeperGameStateField) stateField;
        this.isMarkedAsSave = minesweeperGameStateField.isMarkedAsSafe();
        this.value = minesweeperGameStateField.getValue();
        this.isUncovered = minesweeperGameStateField.isUncovered();
    }

    @Override
    public GameStateField getGameStateField() {
        return null;
    }

    @Override
    public Coordinate getCoordinate() {
        return coordinate;
    }

    public int getValue() {
        return value;
    }

    public boolean isUncovered() {
        return isUncovered;
    }

    public boolean isMarkedAsSave() {
        return isMarkedAsSave;
    }

    public void uncover() {
        this.isUncovered = true;
        invalidate();
    }

    public void reveal() {
        this.isUncovered = true;
        this.wasRevealed = true;
        invalidate();
    }

    @Override
    public void update() {
        // is done by onDraw
    }

    public void toggleMarkedAsSaved() {
        this.isMarkedAsSave = !isMarkedAsSave;
        invalidate();
    }

    public boolean isEmpty() {
        return value == 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isMarkedAsSave) {
            drawFlag(canvas);
            return;
        }

        if (wasRevealed && isBomb()) {
            drawNormalBomb(canvas);
            return;
        }

        if (isUncovered && isBomb()) {
            drawBomb(canvas);
            return;
        }

        if (isUncovered) {
            drawNumber(canvas, value);
            return;
        }

        drawButton(canvas);
    }

    private boolean isDarkThemeOn() {
        return (getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES;
    }

    @Override
    protected void onMeasure(int w, int h) {
        super.onMeasure(w, w);
    }

    private void drawNumber(Canvas canvas, int number) {
        String fileName = "minesweeper_" + number + "" + getFileEnding();
        draw(fileName, canvas);
    }

    private void drawNormalBomb(Canvas canvas) {
        String fileName = "bomb" + getFileEnding();
        draw(fileName, canvas);
    }

    private void drawBomb(Canvas canvas) {
        String fileName = "bomb_exploded" + getFileEnding();
        draw(fileName, canvas);
    }

    private void drawButton(Canvas canvas) {
        String fileName = "button" + getFileEnding();
        draw(fileName, canvas);
    }

    private String getFileEnding() {
        return isDarkThemeOn() ? "_night" : "_light";
    }

    private void drawFlag(Canvas canvas) {
        String fileName = "flag" + getFileEnding();
        draw(fileName, canvas);
    }

    private void draw(@DrawableRes int drawableRes, Canvas canvas) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), drawableRes);
        drawable.setBounds(0, 0, getWidth(), getHeight());
        drawable.draw(canvas);
    }

    private void draw(String drawableResString, Canvas canvas) {
        draw(getContext().getResources().getIdentifier(drawableResString, "drawable", getContext().getPackageName()), canvas);
    }
}
