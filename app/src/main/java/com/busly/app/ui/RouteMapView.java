package com.busly.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.busly.app.R;

/**
 * Mapa estilizado da rota (igual ao protótipo do banner).
 * Desenha o trajeto tracejado e o ônibus na posição indicada por {@link #setProgresso(float)}.
 * Pode ser substituído pelo SDK do Google Maps quando houver dados reais de GPS.
 */
public class RouteMapView extends View {

    private final Paint paintFundo = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintRota = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintOrigem = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintDestino = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintHalo = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintMarcador = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final Path rota = new Path();
    private final PathMeasure medidor = new PathMeasure();
    private final RectF area = new RectF();
    private final float[] ponto = new float[2];

    private Drawable iconeOnibus;
    private float progresso = 0.45f;

    public RouteMapView(Context context) {
        super(context);
        init(context);
    }

    public RouteMapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public RouteMapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paintFundo.setStyle(Paint.Style.FILL);
        paintFundo.setColor(ContextCompat.getColor(context, R.color.busly_menta));

        paintRota.setStyle(Paint.Style.STROKE);
        paintRota.setStrokeWidth(dp(3));
        paintRota.setStrokeCap(Paint.Cap.ROUND);
        paintRota.setColor(ContextCompat.getColor(context, R.color.busly_mapa_rota));
        paintRota.setPathEffect(new DashPathEffect(new float[]{dp(8), dp(7)}, 0f));

        paintOrigem.setColor(ContextCompat.getColor(context, R.color.busly_mapa_origem));
        paintDestino.setColor(ContextCompat.getColor(context, R.color.busly_mapa_destino));

        paintHalo.setColor(ContextCompat.getColor(context, R.color.busly_laranja));
        paintHalo.setAlpha(70);
        paintMarcador.setColor(ContextCompat.getColor(context, R.color.busly_laranja));

        iconeOnibus = ContextCompat.getDrawable(context, R.drawable.ic_bus);
        setContentDescription(context.getString(R.string.descricao_mapa));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        area.set(0, 0, w, h);

        rota.reset();
        rota.moveTo(0.07f * w, 0.86f * h);
        rota.cubicTo(0.22f * w, 0.86f * h, 0.26f * w, 0.58f * h, 0.45f * w, 0.54f * h);
        rota.cubicTo(0.62f * w, 0.50f * h, 0.72f * w, 0.28f * h, 0.93f * w, 0.14f * h);
        medidor.setPath(rota, false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float raio = dp(16);
        canvas.drawRoundRect(area, raio, raio, paintFundo);

        float comprimento = medidor.getLength();
        if (comprimento <= 0f) {
            return;
        }

        canvas.drawPath(rota, paintRota);

        // Origem e destino
        medidor.getPosTan(0f, ponto, null);
        canvas.drawCircle(ponto[0], ponto[1], dp(6), paintOrigem);
        medidor.getPosTan(comprimento, ponto, null);
        canvas.drawCircle(ponto[0], ponto[1], dp(6), paintDestino);

        // Ônibus
        medidor.getPosTan(comprimento * progresso, ponto, null);
        canvas.drawCircle(ponto[0], ponto[1], dp(20), paintHalo);
        canvas.drawCircle(ponto[0], ponto[1], dp(14), paintMarcador);

        int metade = (int) dp(8);
        iconeOnibus.setBounds((int) ponto[0] - metade, (int) ponto[1] - metade,
                (int) ponto[0] + metade, (int) ponto[1] + metade);
        iconeOnibus.draw(canvas);
    }

    /** Posição do ônibus ao longo da rota: 0 = origem, 1 = destino. */
    public void setProgresso(float valor) {
        progresso = Math.max(0f, Math.min(1f, valor));
        invalidate();
    }

    private float dp(float valor) {
        return valor * getResources().getDisplayMetrics().density;
    }
}
