
package com.itextword.text.html.simpleparser;

import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Stack;
import java.util.StringTokenizer;

import com.lowagie.text.Cell;
import com.lowagie.text.Chunk;
import com.lowagie.text.DocListener;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.ElementTags;
import com.lowagie.text.ExceptionConverter;
import com.lowagie.text.FontFactoryImp;
import com.lowagie.text.HeaderFooter;
import com.lowagie.text.Image;
import com.lowagie.text.ListItem;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.Table;
import com.lowagie.text.TextElementArray;
import com.lowagie.text.html.HtmlTags;
import com.lowagie.text.html.Markup;
import com.lowagie.text.html.simpleparser.ALink;
import com.lowagie.text.html.simpleparser.ChainedProperties;
import com.lowagie.text.html.simpleparser.FactoryProperties;
import com.lowagie.text.html.simpleparser.ImageProvider;
import com.lowagie.text.html.simpleparser.Img;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.draw.LineSeparator;
import com.lowagie.text.xml.simpleparser.SimpleXMLDocHandler;
import com.lowagie.text.xml.simpleparser.SimpleXMLParser;

public class HTMLWorker implements SimpleXMLDocHandler, DocListener {

	protected ArrayList<Element> objectList;

	protected DocListener document;
	
	//protected Cell parentCell;
	
	protected Table parentTable;

	private Paragraph currentParagraph;

	private ChainedProperties cprops = new ChainedProperties();

	private Stack<Element> stack = new Stack<Element>();

	private boolean pendingTR = false;

	private boolean pendingTD = false;

	private boolean pendingLI = false;

	private StyleSheet style = new StyleSheet();

	private boolean isPRE = false;

	private Stack<boolean[]> tableState = new Stack<boolean[]>();

	private boolean skipText = false;

	private HashMap<String, Object> interfaceProps;

	private FactoryProperties factoryProperties = new FactoryProperties();

	/** Creates a new instance of HTMLWorker
	 * @param document A class that implements <CODE>DocListener</CODE>
	 * */
	public HTMLWorker(DocListener document,Table parentTable ) {
		//this.document = document;
		this.parentTable = parentTable;
	}
	

private int rows = 0;
	public void setStyleSheet(StyleSheet style) {
		this.style = style;
	}

	public StyleSheet getStyleSheet() {
		return style;
	}

	public void setInterfaceProps(HashMap<String, Object> interfaceProps) {
		this.interfaceProps = interfaceProps;
		FontFactoryImp ff = null;
		if (interfaceProps != null)
			ff = (FontFactoryImp) interfaceProps.get("font_factory");
		if (ff != null)
			factoryProperties.setFontImp(ff);
	}

	public HashMap<String, Object> getInterfaceProps() {
		return interfaceProps;
	}

	public void parse(Reader reader) throws IOException {
		SimpleXMLParser.parse(this, null, reader, true);
	}

	public static ArrayList<Element> parseToList(Reader reader, StyleSheet style)
			throws IOException {
		return parseToList(reader, style, null);
	}

	public static ArrayList<Element> parseToList(Reader reader, StyleSheet style,
			HashMap<String, Object> interfaceProps) throws IOException {
		HTMLWorker worker = new HTMLWorker(null,null);
		if (style != null)
			worker.style = style;
		worker.document = worker;
		//worker.parentCell = new Cell();
		worker.setInterfaceProps(interfaceProps);
		worker.objectList = new ArrayList<Element>();
		worker.parse(reader);
		return worker.objectList;
	}

	public void endDocument() {
		try {
			for (int k = 0; k < stack.size(); ++k){
				Object obj = stack.elementAt(k);
				if(  obj instanceof IncTable ){
					parentTable.insertTable( ((IncTable)obj).buildTable() );
				}
				else{
					Cell cell =  new Cell( stack.elementAt(k) ); 
					/*cell.setBorder( 0 );
					cell.enableBorderSide( 0 );*/
					cell.enableBorderSide( 0);
					parentTable.addCell( cell );
					rows++;
				}
				
			}
			if (currentParagraph != null){
				//document.add(currentParagraph);
				Cell cell = new Cell( currentParagraph );
//				cell.setBorder( 0 );
//				cell.enableBorderSide(  0 );
				cell.enableBorderSide( 0);
				parentTable.addCell( cell );
				rows++;
			}
			currentParagraph = null;
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

    
	public void startDocument() {
		HashMap<String, String> h = new HashMap<String, String>();
		style.applyStyle("body", h);
		cprops.addToChain("body", h);
	}



	public void endElement(String tag) {
//		System.out.println("end:" + tag);
		if (!tagsSupported.contains(tag))
			return;
		try {
			String follow = (String)FactoryProperties.followTags.get(tag);
			if (follow != null) {
				cprops.removeChain(follow);
				return;
			}
			if (tag.equals("font") || tag.equals("span")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals("a")) {
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
				}
				boolean skip = false;
				if (interfaceProps != null) {
					ALink i = (ALink) interfaceProps.get("alink_interface");
					if (i != null)
						skip = i.process(currentParagraph, cprops);
				}
				if (!skip) {
					String href = cprops.getProperty("href");
					if (href != null) {
						//for (Chunk ck : currentParagraph.getChunks()) {
						for( int i = 0; i < currentParagraph.getChunks().size(); i ++){
							Chunk ck = (Chunk)currentParagraph.getChunks().get( i );
							ck.setAnchor(href);
						}
					}
				}
				Paragraph tmp = (Paragraph) stack.pop();
				Phrase tmp2 = new Phrase();
				tmp2.add(currentParagraph);
				tmp.add(tmp2);
				currentParagraph = tmp;
				cprops.removeChain("a");
				return;
			}
			if (tag.equals("br")) {
				return;
			}
			if (currentParagraph != null) {
				if (stack.empty()){
					//document.add(currentParagraph);
					Cell cell = new Cell( currentParagraph );
//					cell.setBorder( 0 );
//					cell.enableBorderSide(  0 );
					cell.enableBorderSide( 0);
					parentTable.addCell(  cell );
					rows++;
				}
				else {
					Element obj = stack.pop();
					if (obj instanceof TextElementArray) {
						TextElementArray current = (TextElementArray) obj;
						current.add(currentParagraph);
					}
					stack.push(obj);
				}
			}
			currentParagraph = null;
			if (tag.equals(HtmlTags.UNORDEREDLIST)
					|| tag.equals(HtmlTags.ORDEREDLIST)) {
				if (pendingLI)
					endElement(HtmlTags.LISTITEM);
				skipText = false;
				cprops.removeChain(tag);
				if (stack.empty())
					return;
				Element obj = stack.pop();
				if (!(obj instanceof com.itextpdf.text.List)) {
					stack.push(obj);
					return;
				}
				if (stack.empty())
				{
					Cell cell = new Cell( obj );
//					cell.setBorder( 0 );
//					cell.enableBorderSide( 0 );
					cell.enableBorderSide( 0);
					parentTable.addCell( cell );
					//document.add(obj);
					rows++;
				}
				else
					((TextElementArray) stack.peek()).add(obj);
				return;
			}
			if (tag.equals(HtmlTags.LISTITEM)) {
				pendingLI = false;
				skipText = true;
				cprops.removeChain(tag);
				if (stack.empty())
					return;
				Element obj = stack.pop();
				if (!(obj instanceof ListItem)) {
					stack.push(obj);
					return;
				}
				if (stack.empty()) {
					//document.add(obj);
					//parentCell.add( obj );
					Cell cell = new Cell( obj );
//					cell.setBorder( 0 );
//					cell.enableBorderSide(  0);
					cell.enableBorderSide( 0);
					parentTable.addCell( cell );
					
					rows++;
					return;
				}
				Element list = stack.pop();
				if (!(list instanceof com.itextpdf.text.List)) {
					stack.push(list);
					return;
				}
				ListItem item = (ListItem) obj;
				(( com.lowagie.text.List) list).add(item);
				ArrayList<Chunk> cks = item.getChunks();
				if (!cks.isEmpty())
					item.getListSymbol()
							.setFont(cks.get(0).getFont());
				stack.push(list);
				return;
			}
			if (tag.equals("div") || tag.equals("body")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals(HtmlTags.PRE)) {
				cprops.removeChain(tag);
				isPRE = false;
				return;
			}
			if (tag.equals("p")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3")
					|| tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
				cprops.removeChain(tag);
				return;
			}
			if (tag.equals("table")) {
				if (pendingTR)
					endElement("tr");
				cprops.removeChain("table");
				IncTable table = (IncTable) stack.pop();
				Table tb = table.buildTable();
//				tb.setWidths(new float[]{0.1f, 0.1f, 0.8f});
				// by liusheng tb.setSplitRows(true);
				if (stack.empty())
				{
					//document.add(tb);
					parentTable.insertTable(tb);
					//parentCell.add( tb );
				}
				else
				{
					((TextElementArray) stack.peek()).add(tb);
				}
				boolean state[] = tableState.pop();
				pendingTR = state[0];
				pendingTD = state[1];
				skipText = false;
				return;
			}
			if (tag.equals("tr")) {
				if (pendingTD)
					endElement("td");
				pendingTR = false;
				cprops.removeChain("tr");
				ArrayList<Cell> cells = new ArrayList<Cell>();
				ArrayList<Integer> cellWidths = new ArrayList<Integer>();
				IncTable table = null;
				while (true) {
					Element obj = stack.pop();
					if (obj instanceof IncCell) {
						//cells.add(((IncCell) obj).getCell());
						IncCell cell = (IncCell)obj;
						if(cell.getWidth() != null){
							cellWidths.add(cell.getWidth());
						}
						
						cells.add(cell.getCell());	
						
					}
					if (obj instanceof IncTable) {
						table = (IncTable) obj;
						break;
					}
				}
				table.addCols(cells);
				if(cellWidths.size()>0){
					Collections.reverse(cellWidths);
					int[] widths = new int[cellWidths.size()];
					for(int i=0;i<widths.length;i++){
						widths[i] = cellWidths.get(i).intValue();
						table.setColWidths(widths);
					}
				}
				table.endRow();
				stack.push(table);
				skipText = true;
				return;
			}
			if (tag.equals("td") || tag.equals("th")) {
				pendingTD = false;
				cprops.removeChain("td");
				skipText = true;
				return;
			}
		} catch (Exception e) {
			throw new ExceptionConverter(e);
		}
	}

	public void text(String str) {
		if (skipText)
			return;
		String content = str;
		if (isPRE) {
			if (currentParagraph == null) {
				currentParagraph = FactoryProperties.createParagraph(cprops);
			}
			Chunk chunk = factoryProperties.createChunk(content, cprops);
			currentParagraph.add(chunk);
			return;
		}
		if (content.trim().length() == 0 && content.indexOf(' ') < 0) {
			return;
		}

		StringBuffer buf = new StringBuffer();
		int len = content.length();
		char character;
		boolean newline = false;
		for (int i = 0; i < len; i++) {
			switch (character = content.charAt(i)) {
			case ' ':
				if (!newline) {
					buf.append(character);
				}
				break;
			case '\n':
				if (i > 0) {
					newline = true;
					buf.append(' ');
				}
				break;
			case '\r':
				break;
			case '\t':
				break;
			default:
				newline = false;
				buf.append(character);
			}
		}
		if (currentParagraph == null) {
			currentParagraph = FactoryProperties.createParagraph(cprops);
		}
		Chunk chunk = factoryProperties.createChunk(buf.toString(), cprops);
		currentParagraph.add(chunk);
	}

	public boolean add(Element element) throws DocumentException {
		objectList.add(element);
		return true;
	}

	public void clearTextWrap() throws DocumentException {
	}

	public void close() {
	}

	public boolean newPage() {
		return true;
	}

	public void open() {
	}

	public void resetPageCount() {
	}

	public boolean setMarginMirroring(boolean marginMirroring) {
		return false;
	}

	/**
     * @see com.itextpdf.text.DocListener#setMarginMirroring(boolean)
	 * @since	2.1.6
	 */
	public boolean setMarginMirroringTopBottom(boolean marginMirroring) {
		return false;
	}

	public boolean setMargins(float marginLeft, float marginRight,
			float marginTop, float marginBottom) {
		return true;
	}

	public void setPageCount(int pageN) {
	}

	public boolean setPageSize(Rectangle pageSize) {
		return true;
	}

	public static final String tagsSupportedString = "ol ul li a pre font span br p div body table td th tr i b u sub sup em strong s strike"
			+ " h1 h2 h3 h4 h5 h6 img hr";

	public static final HashSet<String> tagsSupported = new HashSet<String>();

	static {
		StringTokenizer tok = new StringTokenizer(tagsSupportedString);
		while (tok.hasMoreTokens())
			tagsSupported.add(tok.nextToken());
	}

	public void resetFooter() {
	
		
	}

	public void resetHeader() {
		
	}

	public void setFooter(HeaderFooter arg0) {
		
	}

	public void setHeader(HeaderFooter arg0) {
		
	}

	@Override
	public void startElement(String tag, HashMap h) {
//		System.out.println( tag );
		if (!tagsSupported.contains(tag))
			return;
		try {
			style.applyStyle(tag, h);
			String follow = (String)FactoryProperties.followTags.get(tag);
			if (follow != null) {
				HashMap<String, String> prop = new HashMap<String, String>();
				prop.put(follow, null);
				cprops.addToChain(follow, prop);
				return;
			}
			FactoryProperties.insertStyle(h, cprops);
			if (tag.equals(HtmlTags.ANCHOR)) {
				cprops.addToChain(tag, h);
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
				}
				stack.push(currentParagraph);
				currentParagraph = new Paragraph();
				return;
			}
			if (tag.equals(HtmlTags.NEWLINE)) {
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
				}
				currentParagraph.add(factoryProperties
						.createChunk("\n", cprops));
				return;
			}
			if (tag.equals(HtmlTags.HORIZONTALRULE)) {
				// Attempting to duplicate the behavior seen on Firefox with
				// http://www.w3schools.com/tags/tryit.asp?filename=tryhtml_hr_test
				// where an initial break is only inserted when the preceding element doesn't
				// end with a break, but a trailing break is always inserted.
				boolean addLeadingBreak = true;
				if (currentParagraph == null) {
					currentParagraph = new Paragraph();
					addLeadingBreak = false;
				}
				if (addLeadingBreak) { // Not a new paragraph
					int numChunks = currentParagraph.getChunks().size();
					if (numChunks == 0 ||
							((Chunk)(currentParagraph.getChunks().get(numChunks - 1))).getContent().endsWith("\n"))
						addLeadingBreak = false;
				}
				String align = (String)h.get("align");
				int hrAlign = Element.ALIGN_CENTER;
				if (align != null) {
					if (align.equalsIgnoreCase("left"))
						hrAlign = Element.ALIGN_LEFT;
					if (align.equalsIgnoreCase("right"))
						hrAlign = Element.ALIGN_RIGHT;
				}
				String width = (String)h.get("width");
				float hrWidth = 1;
				if (width != null) {
					float tmpWidth = Markup.parseLength(width, Markup.DEFAULT_FONT_SIZE);
					if (tmpWidth > 0) hrWidth = tmpWidth;
					if (!width.endsWith("%"))
						hrWidth = 100; // Treat a pixel width as 100% for now.
				}
				String size = (String)h.get("size");
				float hrSize = 1;
				if (size != null) {
					float tmpSize = Markup.parseLength(size, Markup.DEFAULT_FONT_SIZE);
					if (tmpSize > 0)
						hrSize = tmpSize;
				}
				if (addLeadingBreak)
					currentParagraph.add(Chunk.NEWLINE);
				currentParagraph.add(new LineSeparator(hrSize, hrWidth, null, hrAlign, currentParagraph.getLeading()/2));
				currentParagraph.add(Chunk.NEWLINE);
				return;
			}
			if (tag.equals(HtmlTags.CHUNK) || tag.equals(HtmlTags.SPAN)) {
				cprops.addToChain(tag, h);
				return;
			}
			if (tag.equals(HtmlTags.IMAGE)) {
				String src = (String)h.get(ElementTags.SRC);
				if (src == null)
					return;
				cprops.addToChain(tag, h);
				Image img = null;
				if (interfaceProps != null) {
					ImageProvider ip = (ImageProvider) interfaceProps
							.get("img_provider");
					if (ip != null){
						//img = ip.getImage(src, h, cprops, document);
					}
					if (img == null) {
						HashMap<String, Image> images = (HashMap<String, Image>) interfaceProps
								.get("img_static");
						if (images != null) {
							Image tim = images.get(src);
							if (tim != null)
								img = Image.getInstance(tim);
						} else {
							if (!src.startsWith("http")) { // relative src references only
								String baseurl = (String) interfaceProps
										.get("img_baseurl");
								if (baseurl != null) {
									src = baseurl + src;
									img = Image.getInstance(src);
								}
							}
						}
					}
				}
				if (img == null) {
					if (!src.startsWith("http")) {
						String path = cprops.getProperty("image_path");
						if (path == null)
							path = "";
						src = new File(path, src).getPath();
					}
					img = Image.getInstance(src);
				}
				String align = (String)h.get("align");
				String width = (String)h.get("width");
				String height = (String)h.get("height");
				String before = cprops.getProperty("before");
				String after = cprops.getProperty("after");
				if (before != null)
					img.setSpacingBefore(Float.parseFloat(before));
				if (after != null)
					img.setSpacingAfter(Float.parseFloat(after));
				float actualFontSize = Markup.parseLength(cprops
						.getProperty(ElementTags.SIZE),
						Markup.DEFAULT_FONT_SIZE);
				if (actualFontSize <= 0f)
					actualFontSize = Markup.DEFAULT_FONT_SIZE;
				float widthInPoints = Markup.parseLength(width, actualFontSize);
				float heightInPoints = Markup.parseLength(height,
						actualFontSize);
				if (widthInPoints > 0 && heightInPoints > 0) {
					img.scaleAbsolute(widthInPoints, heightInPoints);
				} else if (widthInPoints > 0) {
					heightInPoints = img.getHeight() * widthInPoints
							/ img.getWidth();
					img.scaleAbsolute(widthInPoints, heightInPoints);
				} else if (heightInPoints > 0) {
					widthInPoints = img.getWidth() * heightInPoints
							/ img.getHeight();
					img.scaleAbsolute(widthInPoints, heightInPoints);
				}
				img.setWidthPercentage(0);
				if (align != null) {
					endElement("p");
					int ralign = Image.MIDDLE;
					if (align.equalsIgnoreCase("left"))
						ralign = Image.LEFT;
					else if (align.equalsIgnoreCase("right"))
						ralign = Image.RIGHT;
					img.setAlignment(ralign);
					Img i = null;
					boolean skip = false;
					if (interfaceProps != null) {
						i = (Img) interfaceProps.get("img_interface");
						if (i != null){
							//skip = i.process(img, h, cprops, document);
						}
					}
					if (!skip){
						//document.add(img);
						Cell cell = new Cell( img );
//						cell.setBorder( 0 );
//						cell.enableBorderSide( 0 );
						cell.enableBorderSide( 0);
						parentTable.addCell( cell );
						rows++;
					}
					cprops.removeChain(tag);
					
				} else {
					cprops.removeChain(tag);
					if (currentParagraph == null) {
						currentParagraph = FactoryProperties
								.createParagraph(cprops);
					}
					currentParagraph.add(new Chunk(img, 0, 0));
				}
				return;
			}
			endElement("p");
			if (tag.equals("h1") || tag.equals("h2") || tag.equals("h3")
					|| tag.equals("h4") || tag.equals("h5") || tag.equals("h6")) {
				if (!h.containsKey(ElementTags.SIZE)) {
					int v = 7 - Integer.parseInt(tag.substring(1));
					h.put(ElementTags.SIZE, Integer.toString(v));
				}
				cprops.addToChain(tag, h);
				return;
			}
			if (tag.equals(HtmlTags.UNORDEREDLIST)) {
				if (pendingLI)
					endElement(HtmlTags.LISTITEM);
				skipText = true;
				cprops.addToChain(tag, h);
				 com.lowagie.text.List list = new  com.lowagie.text.List(false);
				try{
					list.setIndentationLeft(new Float(cprops.getProperty("indent")).floatValue());
				}catch (Exception e) {
					list.setAutoindent(true);
				}
				list.setListSymbol("\u2022");
				stack.push(list);
				return;
			}
			if (tag.equals(HtmlTags.ORDEREDLIST)) {
				if (pendingLI)
					endElement(HtmlTags.LISTITEM);
				skipText = true;
				cprops.addToChain(tag, h);
				 com.lowagie.text.List list = new  com.lowagie.text.List(true);
				try{
					list.setIndentationLeft(new Float(cprops.getProperty("indent")).floatValue());
				}catch (Exception e) {
					list.setAutoindent(true);
				}
				stack.push(list);
				return;
			}
			if (tag.equals(HtmlTags.LISTITEM)) {
				if (pendingLI)
					endElement(HtmlTags.LISTITEM);
				skipText = false;
				pendingLI = true;
				cprops.addToChain(tag, h);
				ListItem item = FactoryProperties.createListItem(cprops);
				stack.push(item);
				return;
			}
			if (tag.equals(HtmlTags.DIV) || tag.equals(HtmlTags.BODY) || tag.equals("p")) {
				cprops.addToChain(tag, h);
				return;
			}
			if (tag.equals(HtmlTags.PRE)) {
				if (!h.containsKey(ElementTags.FACE)) {
					h.put(ElementTags.FACE, "Courier");
				}
				cprops.addToChain(tag, h);
				isPRE = true;
				return;
			}
			if (tag.equals("tr")) {
				if (pendingTR)
					endElement("tr");
				skipText = true;
				pendingTR = true;
				cprops.addToChain("tr", h);
				return;
			}
			if (tag.equals("td") || tag.equals("th")) {
				if (pendingTD)
					endElement(tag);
				skipText = false;
				pendingTD = true;
				cprops.addToChain("td", h);
				stack.push(new IncCell(tag, cprops));
				return;
			}
			if (tag.equals("table")) {
				cprops.addToChain("table", h);
				IncTable table = new IncTable(h);
				stack.push(table);
				tableState.push(new boolean[] { pendingTR, pendingTD });
				pendingTR = pendingTD = false;
				skipText = true;
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//throw new ExceptionConverter(e);
		}		
	}

	public int getRows() {
		return rows;
	}

	public void setRows(int rows) {
		this.rows = rows;
	}
	

}