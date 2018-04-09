import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.*;


public class ImageApp   {
    
    // Leitura da imagem
    public static BufferedImage loadImage(String surl) {  
        BufferedImage bimg = null;  
        try {  
            URL url = new URL(surl);
            bimg = ImageIO.read(url);  
            //bimg = ImageIO.read(new File("D:/Temp/mundo.jpg"));
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return bimg;  
    }  
    
    public void apresentaImagem(JFrame frame, BufferedImage img) {
        frame.setBounds(0, 0, img.getWidth(), img.getHeight());
        JImagePanel panel = new JImagePanel(img, 0, 0);
        frame.add(panel);
        frame.setVisible(true);
    }
    
    public static BufferedImage criaImagemRGB() {
        BufferedImage img = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);

        WritableRaster raster = img.getRaster();
        
        for(int h=0;h<200;h++) //200 � o tamanho e w e h � a posi��o x e y
            for(int w=0;w<200;w++) {//refere-se a imagem amarela
                raster.setSample(w,h,0,220); // Componente Vermelho
                raster.setSample(w,h,1,219); // Componente Verde
                raster.setSample(w,h,2,97);  // Componente Azul
            } 
        return img;
    }
    
    public static BufferedImage criaImagemCinza(BufferedImage img_orig) {
        BufferedImage img_gray = new BufferedImage(img_orig.getWidth(), img_orig.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster_gray = img_gray.getRaster();
        for(int h=0;h<img_gray.getHeight();h++)
            for(int w=0;w<img_gray.getWidth();w++) {
                int value = img_orig.getRGB(w,h); 
                int r = (value & 0x00ff0000) >> 16;
                int g = (value & 0x0000ff00) >> 8;
                int b =  value & 0x000000ff;
                raster_gray.setSample(w,h,0, 0.21*r + 0.72*g + 0.07*b);//como o h = 0 e vai aumentando, cada linha vai ficando mais clara
            }
        return img_gray;
    }
    
    public static BufferedImage criaImagemBinaria(BufferedImage img_orig) {
        BufferedImage img_binary = new BufferedImage(img_orig.getWidth(), img_orig.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        WritableRaster raster_binary = img_binary.getRaster();
        for(int h=0;h<img_binary.getHeight();h++)
            for(int w=0;w<img_binary.getWidth();w++) {
                int value = img_orig.getRGB(w,h); 
                int r = (value & 0x00ff0000) >> 16;
                int g = (value & 0x0000ff00) >> 8;
                int b =  value & 0x000000ff;
                raster_binary.setSample(w,h,0, (0.21*r + 0.72*g + 0.07*b) > 126 ? 1 : 0); 
            }
        return img_binary;
    }
    
    // Imprime valores dos pixeis de imagem RGB
    public static void  imprimePixeis(BufferedImage bufferedImage) {
        // Get a pixel
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        
        for(int h=0;h<height;h++)
            for(int w=0;w<width;w++) {
                int rgb = bufferedImage.getRGB(w,h);
                int r = (int)((rgb&0x00FF0000)>>>16); // componente vermelho
                int g = (int)((rgb&0x0000FF00)>>>8); // componente verde
                int b = (int)(rgb&0x000000FF); //componente azul
                System.out.print("at ("+w+","+h+"): ");
                System.out.println(r+","+g+","+b);
            }
    }

    public static void main(String[] args) {
        ImageApp ia = new ImageApp();
        BufferedImage imgJPEG = loadImage("http://www.inf.ufsc.br/~willrich/INE5431/RGB.jpg");
        BufferedImage imgReduzida = reduz_imagem(imgJPEG,4);
        BufferedImage imgCinza = criaImagemCinza(imgJPEG);
        BufferedImage imgBinaria = criaImagemBinaria(imgJPEG);
        BufferedImage imgVermelho = criaImagemVermelho(imgJPEG);
        BufferedImage imgVerde = criaImagemVerde(imgJPEG);
        BufferedImage imgAzul = criaImagemAzul(imgJPEG);

        ia.apresentaImagem(new JFrame("imgJPEG"), imgJPEG);
        ia.apresentaImagem(new JFrame("imgJPEGreduzida"), imgReduzida);
        ia.apresentaImagem(new JFrame("imgCinza"), imgCinza);
        ia.apresentaImagem(new JFrame("imgBinaria"), imgBinaria);
        ia.apresentaImagem(new JFrame("imgVermelho"), imgVermelho);
        ia.apresentaImagem(new JFrame("imgVerde"), imgVerde);
        ia.apresentaImagem(new JFrame("imgAzul"), imgAzul);
               
        imprimePixeis(imgJPEG);
    }

    public static BufferedImage reduz_imagem(BufferedImage img_orig, int fator_reducao)
    {
        BufferedImage img_reduzida = new BufferedImage(img_orig.getWidth()/fator_reducao,
                                                       img_orig.getHeight()/fator_reducao,
                                                       BufferedImage.TYPE_INT_RGB);
        WritableRaster raster_orig = img_orig.getRaster(),
                       raster_reduzida = img_reduzida.getRaster();

        int h_aux = 0, w_aux = 0;
        for(int i = 0; i < img_orig.getHeight(); i += fator_reducao) {
            w_aux = 0;
            for(int j = 0; j < img_orig.getWidth(); j += fator_reducao) {
                raster_reduzida.setSample(w_aux, h_aux, 0, raster_orig.getSample(j,i,0)); //B
                raster_reduzida.setSample(w_aux, h_aux, 1, raster_orig.getSample(j,i,1)); //G
                raster_reduzida.setSample(w_aux, h_aux, 2, raster_orig.getSample(j,i,2)); //R
                ++w_aux;
            }
            ++h_aux;
        }
        return img_reduzida;
    }

    public static BufferedImage criaImagemVermelho(BufferedImage img_orig) {
        BufferedImage img_gray = new BufferedImage(img_orig.getWidth(), img_orig.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster_gray = img_gray.getRaster();
        for(int h=0;h<img_gray.getHeight();h++)
            for(int w=0;w<img_gray.getWidth();w++) {
                int value = img_orig.getRGB(w,h); 
                int r = (value & 0x00ff0000) >> 16;
                int g = (value & 0x0000ff00) >> 8;
                int b =  value & 0x000000ff;
                raster_gray.setSample(w,h,0,r);//como o h = 0 e vai aumentando, cada linha vai ficando mais clara
            }
        return img_gray;
    }

    public static BufferedImage criaImagemVerde(BufferedImage img_orig) {
        BufferedImage img_gray = new BufferedImage(img_orig.getWidth(), img_orig.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster_gray = img_gray.getRaster();
        for(int h=0;h<img_gray.getHeight();h++)
            for(int w=0;w<img_gray.getWidth();w++) {
                int value = img_orig.getRGB(w,h); 
                int r = (value & 0x00ff0000) >> 16;
                int g = (value & 0x0000ff00) >> 8;
                int b =  value & 0x000000ff;
                raster_gray.setSample(w,h,0,g);//como o h = 0 e vai aumentando, cada linha vai ficando mais clara
            }
        return img_gray;
    }

    public static BufferedImage criaImagemAzul(BufferedImage img_orig) {
        BufferedImage img_gray = new BufferedImage(img_orig.getWidth(), img_orig.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        WritableRaster raster_gray = img_gray.getRaster();
        for(int h=0;h<img_gray.getHeight();h++)
            for(int w=0;w<img_gray.getWidth();w++) {
                int value = img_orig.getRGB(w,h); 
                int r = (value & 0x00ff0000) >> 16;
                int g = (value & 0x0000ff00) >> 8;
                int b =  value & 0x000000ff;
                raster_gray.setSample(w,h,0,b);//como o h = 0 e vai aumentando, cada linha vai ficando mais clara
            }
        return img_gray;
    }

}