package org.eclipse.sprotty;


/**
 * Whether and when a diagram server needs to compute the layout of the model. 
 * 
 * The layout is computed with the layout engine configured with {@link DefaultDiagramServer#setLayoutEngine(ILayoutEngine)}, so 
 * a value other than <code>NONE</code> here makes sense only if such an engine is available.
 */
public enum ServerLayoutKind {
	/**
	 * The server re-layouts the diagram on all changes automatically. 
	 * Layout information stored in the model will be overwritten.
	 */
	AUTOMATIC, 

	/**
	 * The server re-layouts the diagram interactively. In ELK, <em>interactive</em> means
	 * that the order is not calculated but derived from the positions of the nodes. This
	 * is close to a partial layout.  
	 * The layout information must be stored in the model and will be overwritten on layout. 
	 */
	INTERACTIVE, 
	
	/**
	 * The server re-layouts the diagram only if manually triggered by a <code>LayoutAction</code>.
	 * The layout information must be stored in the model and will be overwritten on layout. 
	 */
	MANUAL,
	
	/**
	 * The server never layouts the diagram. 
	 * This requires that the layout information is stored in the model.
	 */
	NONE 
}
