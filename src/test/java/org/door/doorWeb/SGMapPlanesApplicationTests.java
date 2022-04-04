package org.door.doorWeb;

import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class SGMapPlanesApplicationTests {

	@Test
	void readJSON() throws JSONException, IOException {
		ArrayList<String> doors = DoorStatusBean.getValidDoors("data.json");
		assertTrue(doors instanceof ArrayList);
	}
}
