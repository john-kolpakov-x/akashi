package kz.pompei.akashi;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;

public class Hello {
  public static void main(String[] args) throws Exception {
    new Hello().run();
  }

  private void run() throws Exception {
    BufferedImage image = new BufferedImage(1000, 600, BufferedImage.TYPE_INT_ARGB);

    {
      Graphics2D g = image.createGraphics();
      applyHints(g);
      draw(g, image.getWidth(), image.getHeight());
      g.dispose();
    }

    {
      File file = new File("build/Hello.png");
      file.getParentFile().mkdirs();
      ImageIO.write(image, "png", file);
    }
  }

  private void applyHints(Graphics2D g) {
    g.setRenderingHints(new RenderingHints(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    ));
    g.setRenderingHints(new RenderingHints(
        RenderingHints.KEY_ANTIALIASING,
        RenderingHints.VALUE_ANTIALIAS_ON
    ));
  }

  private static Area ellipse(float d) {
    return new Area(new Ellipse2D.Float(-0.5f * d, -0.5f * d, d, d));
  }

  private static Area rect(float x1, float y1, float x2, float y2) {
    return new Area(new Rectangle2D.Float(x1, y1, x2 - x1, y2 - y1));
  }

  private void draw(Graphics2D g, float width, float height) {
    float D = (width < height ? width : height) * 0.8f;
    float x = width / 2, y = height / 2;

    AffineTransform tx = new AffineTransform();
    tx.translate(x, y);
    tx.scale(D, D);

    Area e = ellipse(0.9f);
    Area e1 = ellipse(0.85f);

    Area a = new Area();
    a.add(e);
    a.subtract(e1);

    float delta = 0.07f, K = 0.3f;

    for (float X = -0.5f; X < 0.59f; X += delta) {
      a.add(rect(X, -0.5f, X + delta * K, 0.5f));
    }

    g.setColor(Color.GREEN.darker());
    g.fill(tx.createTransformedShape(a));
  }
}
