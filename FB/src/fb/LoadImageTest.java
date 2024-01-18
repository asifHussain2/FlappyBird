package fb;

import static org.junit.Assert.*;

import org.junit.Test;

public class LoadImageTest {

	@Test
	public void test() {
		FlappyBirdGame game = new FlappyBirdGame("Easy",3);
		GamePanel p= new GamePanel(game);
		boolean output= p.loadImages();
		assertEquals(true,output);
		
		
	}

}
