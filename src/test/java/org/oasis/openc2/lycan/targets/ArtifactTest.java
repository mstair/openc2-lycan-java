/* 
 * The MIT License (MIT)
 *
 * Copyright 2018 AT&T Intellectual Property. All other rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS
 * FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN
 * AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */
package org.oasis.openc2.lycan.targets;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.oasis.openc2.lycan.OpenC2Message;
import org.oasis.openc2.lycan.action.ActionType;
import org.oasis.openc2.lycan.json.JsonFormatter;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ArtifactTest {
	private static final boolean toConsole = false;
	private static final String MIME_VALUE = "Test mime";
	private static final Byte[] PAYLOAD_VALUE = {10,15,20};
	private static final String URL_VALUE = "test url";
	private static final String HASHKEY1_VALUE = "hashkey1";
	private static final String VALUE1_VALUE = "value1";
	private static final String HASHKEY2_VALUE = "hashkey2";
	private static final String VALUE2_VALUE = "value2";
	private static final Map<String, Object> MAP_VALUE;
	
	private static final String expect = "{\"action\":\"report\",\"target\":{\"artifact\":{\"mime_type\":\"Test mime\",\"payload_bin\":[10,15,20],\"url\":\"test url\",\"hashes\":{\"hashkey2\":\"value2\",\"hashkey1\":\"value1\"}}}}";

	static {
		MAP_VALUE = new HashMap<String, Object>();
		MAP_VALUE.put(HASHKEY1_VALUE, VALUE1_VALUE);
		MAP_VALUE.put(HASHKEY2_VALUE, VALUE2_VALUE);
	}
	
	@Test
	public void test1() throws Exception {

		Artifact target = new Artifact()
				.setMime(MIME_VALUE)
				.setPayloadBin(PAYLOAD_VALUE)
				.setUrl(URL_VALUE)
				.setHashes(MAP_VALUE);
		OpenC2Message message = new OpenC2Message(ActionType.REPORT, target);

		JsonNode expected = new ObjectMapper().readTree(expect);
		JsonNode actual = new ObjectMapper().readTree(message.toJson());
		assertEquals(expected, actual);

		if (toConsole) {
    		// This is just to allow developer to eyeball the JSON created
    		System.out.println("");
    		System.out.println("ArtifactTest - Test1 JSON output:");
    		System.out.println(message.toJson());
			System.out.println(message.toPrettyJson());
			System.out.println("\n\n");
		}

		OpenC2Message inMsg = JsonFormatter.readOpenC2Message(expect);
		assertTrue(inMsg.getTarget() instanceof Artifact);
		Artifact inTarget = (Artifact)inMsg.getTarget();
		assertEquals(MIME_VALUE, inTarget.getMime());
		assertArrayEquals(PAYLOAD_VALUE, inTarget.getPayloadBin());
		assertEquals(URL_VALUE, inTarget.getUrl());
		assertEquals(MAP_VALUE, inTarget.getHashes());
		
	}

}
