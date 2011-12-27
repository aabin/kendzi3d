/*
 * This software is provided "AS IS" without a warranty of any kind.
 * You use it on your own risk and responsibility!!!
 *
 * This file is shared under BSD v3 license.
 * See readme.txt and BSD3 file for details.
 *
 */

package kendzi.josm.kendzi3d.jogl.layer;

import java.util.ArrayList;
import java.util.List;

import kendzi.josm.kendzi3d.jogl.model.Model;
import kendzi.josm.kendzi3d.jogl.model.Perspective3D;
import kendzi.josm.kendzi3d.jogl.model.trees.Forest;
import kendzi.josm.kendzi3d.jogl.model.trees.Tree;
import kendzi.josm.kendzi3d.jogl.model.trees.TreeRow;

import org.apache.log4j.Logger;
import org.openstreetmap.josm.actions.search.SearchCompiler;
import org.openstreetmap.josm.actions.search.SearchCompiler.Match;
import org.openstreetmap.josm.actions.search.SearchCompiler.ParseError;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Relation;
import org.openstreetmap.josm.data.osm.Way;

public class TreeLayer implements Layer {

    /** Log. */
    @SuppressWarnings("unused")
    private static final Logger log = Logger.getLogger(TreeLayer.class);

    /**
     * List of layer models.
     */
    private List<Model> modelList = new ArrayList<Model>();


    private Match treesMatcher;
    private Match treesWayMatcher;

    {
        try {
            this.treesMatcher = SearchCompiler.compile("(natural=tree)", false, false);
        } catch (ParseError e) {
            this.treesMatcher = new SearchCompiler.Never();
            e.printStackTrace();
        }
        try {
            this.treesWayMatcher = SearchCompiler.compile("(natural=tree_row | natural=wood | landuse=forest)", false, false);
        } catch (ParseError e) {
            this.treesWayMatcher = new SearchCompiler.Never();
            e.printStackTrace();
        }


    }

    @Override
    public
    Match getNodeMatcher() {
        return this.treesMatcher;
    }

    @Override
    public Match getWayMatcher() {
        return this.treesWayMatcher;
    }

    @Override
    public Match getRelationMatcher() {
        return null;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public List<Model> getModels() {
        return this.modelList;
    }

    @Override
    public void addModel(Node pNode, Perspective3D pPerspective3D) {
        this.modelList.add(new Tree(pNode, pPerspective3D));

    }

    @Override
    public void addModel(Way pWay, Perspective3D pPerspective3D) {
        if ("tree_row".equals(pWay.get("natural"))) {
            this.modelList.add(new TreeRow(pWay, pPerspective3D));
        } else {
            this.modelList.add(new Forest(pWay, pPerspective3D));
        }
    }

    @Override
    public void addModel(Relation pRelation, Perspective3D pPerspective3D) {
        //
    }

    @Override
    public void clear() {
        this.modelList.clear();
    }

}