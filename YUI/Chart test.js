YUI().use('charts', 'event', 'io', 'json-parse', function (Y) {
		
	var uri = '/garduino/SensorData/1/temp/1';
	
	var myDataValues = [];
	var mychart;
	
	function processDataSet (id, o, args) {
		var id = id;
		alert(o.responseText);
		
		try {
			var response = o.responseText;
			var data = Y.JSON.parse(response);
		} catch(e) {
			alert("can't parse" + e);
		}
		
		// Map objects from server into chart dataset
		for(var i = 0; i < data.length; i++) {
			myDataValues.push({"Date": data[i].timestamp, "Value": data[i].value});
		}
		
		myChart = new Y.Chart({
		    dataProvider: myDataValues,
		    render: "#mychart"
		});
	}
	
	Y.on('io:complete', processDataSet, Y);
	var request = Y.io(uri);
});

