/**
 *  Copyright 2011 Diego Ceccarelli
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package it.cnr.isti.hpc.dexter.cli.graph;

import it.cnr.isti.hpc.cli.AbstractCommandLineInterface;
import it.cnr.isti.hpc.dexter.graph.CategoryNodeFactory;
import it.cnr.isti.hpc.dexter.graph.Node;
import it.cnr.isti.hpc.dexter.graph.NodeFactory;
import it.cnr.isti.hpc.dexter.graph.NodesWriter;
import it.cnr.isti.hpc.io.reader.RecordReader;
import it.cnr.isti.hpc.log.ProgressLogger;
import it.cnr.isti.hpc.property.ProjectProperties;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IndexOutcomingNodesCLI generate the binary file containing the outcoming
 * entities. Takes a file containing the outcoming categories in the format: <br>
 * <br>
 * <code>
 * 	entity/categories_id <tab> e1 e2 ... eN  
 * </code> <br>
 * <br>
 * where <code> e1 e2 .. eN </code> are the entities linking to the given
 * entity <code> category/entity_id </code>. The file is sorted by <code>entity_id</code>
 * , and in each line the outcoming entities are sorted by their numerical id.
 * 
 * @author Diego Ceccarelli, diego.ceccarelli@isti.cnr.it created on 21/nov/2011
 */
public class IndexOutcomingCategoryNodesCLI extends AbstractCommandLineInterface {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(IndexOutcomingCategoryNodesCLI.class);

	private static String[] params = new String[] { "input" };

	private static final String USAGE = "java -cp $jar "
			+ IndexOutcomingCategoryNodesCLI.class + " -input outcoming-categories-file";

	public static void main(String[] args) {
		IndexOutcomingCategoryNodesCLI cli = new IndexOutcomingCategoryNodesCLI(args);
		ProjectProperties properties = new ProjectProperties(
				IndexOutcomingCategoryNodesCLI.class);

		File outcomingFile = new File(properties.get("data.dir"),
				properties.get("ram.outcoming.category.nodes"));

		if (outcomingFile.exists()) {
			logger.info("serialized file {} yet exists, removing",
					outcomingFile);
			outcomingFile.delete();
		}
		ProgressLogger pl = new ProgressLogger("indexed {} nodes", 100000);
		RecordReader<Node> reader = new RecordReader<Node>(cli.getInput(),
				new Node.Parser());
		NodesWriter writer = CategoryNodeFactory
				.getOutcomingNodeWriter(CategoryNodeFactory.STD_TYPE);
		for (Node n : reader) {
			pl.up();
			writer.add(n);
		}
		writer.close();

	}

	public IndexOutcomingCategoryNodesCLI(String[] args) {
		super(args, params, USAGE);
	}
}
