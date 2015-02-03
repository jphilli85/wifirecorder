package com.flat.localization;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;

/**
 * @author Jacob Phillips (02/2015, jphilli85 at gmail)
 */
public class NodeMessage {

    public static final int TYPE_ID = 1;
    public static final int TYPE_RANGE_TABLE = 2;
    public static final int TYPE_COORDINATE_SYSTEM = 3;

    public static final String KEY_TYPE = "type";
    public static final String KEY_NODE_ID = "id";
    public static final String KEY_RANGE_TABLE = "range_table";
//    public static final String KEY_RANGE = "range";
//    public static final String KEY_TIMESTAMP = "time";
    public static final String KEY_COORDINATE_SYSTEM = "coord_system";
//    public static final String KEY_COORDINATES = "coords";

    private JSONObject msg = new JSONObject();

    public int type;
    public String fromId;
    public CoordinateSystem.RangeTable rangeTable;
    public CoordinateSystem coordinateSystem;

    private NodeMessage() {}

    public NodeMessage(String fromId) throws JSONException {
        type = TYPE_ID;
        this.fromId = fromId;
        msg.put(KEY_TYPE, TYPE_ID);
        msg.put(KEY_NODE_ID, fromId);
    }

    public NodeMessage(String fromId, CoordinateSystem.RangeTable rangeTable) throws JSONException {
        type = TYPE_RANGE_TABLE;
        this.fromId = fromId;
        this.rangeTable = rangeTable;
        msg.put(KEY_TYPE, TYPE_RANGE_TABLE);
        msg.put(KEY_NODE_ID, fromId);

        JSONArray ranges = new JSONArray();
        for (String node : rangeTable.keySet()) {
            JSONArray json = new JSONArray();
            json.put(node);
            json.put(rangeTable.get(node).range);
            json.put(rangeTable.get(node).time);
            ranges.put(json);
        }
        msg.put(KEY_RANGE_TABLE, ranges);

        if (rangeTable.coords != null) {
            JSONArray coords = new JSONArray();
            for (String node : rangeTable.coords.keySet()) {
                JSONArray json = new JSONArray();
                json.put(node);
                for (float f : rangeTable.coords.get(node)) {
                    json.put(f);
                }
                coords.put(json);
            }
            msg.put(KEY_COORDINATE_SYSTEM, coords);
        }
    }

    public NodeMessage(String fromId, CoordinateSystem coordinateSystem) throws JSONException {
        type = TYPE_COORDINATE_SYSTEM;
        this.fromId = fromId;
        this.coordinateSystem = coordinateSystem;
        msg.put(KEY_TYPE, TYPE_COORDINATE_SYSTEM);
        msg.put(KEY_NODE_ID, fromId);

        JSONArray coords = new JSONArray();
        for (String node : coordinateSystem.keySet()) {
            JSONArray json = new JSONArray();
            json.put(node);
            for (float f : coordinateSystem.get(node)) {
                json.put(f);
            }
            coords.put(json);
        }
        msg.put(KEY_COORDINATE_SYSTEM, coords);
    }

    public static NodeMessage from(String jsonString) throws JSONException {
        NodeMessage nm = new NodeMessage();
        nm.msg = new JSONObject(jsonString);
        nm.fromId = nm.msg.getString(KEY_NODE_ID);
        nm.type = nm.msg.getInt(KEY_TYPE);
        switch (nm.type) {
            case TYPE_ID:
                // nothing more to do
                break;
            case TYPE_RANGE_TABLE:
                JSONArray coordsArray = nm.msg.optJSONArray(KEY_COORDINATE_SYSTEM);
                if (coordsArray != null) {
                    TreeMap<String, float[]> coords = new TreeMap<String, float[]>();
                    for (int i = 0; i < coordsArray.length(); ++i) {
                        JSONArray json = coordsArray.getJSONArray(i);
                        coords.put(json.getString(0), new float[] {
                                (float) json.getDouble(1),
                                (float) json.getDouble(2),
                                (float) json.getDouble(3)
                        });
                    }
                    nm.rangeTable = new CoordinateSystem.RangeTable(coords);
                } else {
                    nm.rangeTable = new CoordinateSystem.RangeTable(null);
                }

                JSONArray ranges = nm.msg.getJSONArray(KEY_RANGE_TABLE);
                for (int i = 0; i < ranges.length(); ++i) {
                    JSONArray json = ranges.getJSONArray(i);
                    String node = json.getString(0);
                    CoordinateSystem.SimpleRange range = new CoordinateSystem.SimpleRange();
                    range.range = (float) json.getDouble(1);
                    range.time = json.getLong(2);
                    nm.rangeTable.put(node, range);
                }
                break;
            case TYPE_COORDINATE_SYSTEM:
                JSONArray coordsSys = nm.msg.getJSONArray(KEY_COORDINATE_SYSTEM);
                nm.coordinateSystem = new CoordinateSystem();
                for (int i = 0; i < coordsSys.length(); ++i) {
                    JSONArray json = coordsSys.getJSONArray(i);
                    nm.coordinateSystem.put(json.getString(0), new float[] {
                            (float) json.getDouble(1),
                            (float) json.getDouble(2),
                            (float) json.getDouble(3)
                    });
                }
                break;
        }
        return nm;
    }

    @Override
    public String toString() {
        return msg.toString();
    }
}
