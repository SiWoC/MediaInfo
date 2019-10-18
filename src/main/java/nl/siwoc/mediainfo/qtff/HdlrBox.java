package nl.siwoc.mediainfo.qtff;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class HdlrBox extends FullBox {

    private String componentType;
    private String handlerType;

	public String getComponentType() {
		return componentType;
	}

	public void setComponentType(String componentType) {
		this.componentType = componentType;
	}

	public String getHandlerType() {
		return handlerType;
	}

	public void setHandlerType(String handlerType) {
		this.handlerType = handlerType;
	}

	public HdlrBox(Box parent, int size, byte[] data) throws Exception {
		setType("hdlr");
		setSize(size);
		setParent(parent);
		LOGGER.info("Creating " + getType());
		try (InputStream is = new ByteArrayInputStream(data)){
	        setVersion(is.read());
	        setFlag(QTFFUtils.readFlag(is));
	        setComponentType(QTFFUtils.readFourCC(is));
	        setHandlerType(QTFFUtils.readFourCC(is));
	        if (getHandlerType().matches("vide|soun")) {
		        TrakBox trak = (TrakBox)searchUp("trak");
		        if (trak != null) {
		        	trak.setHandlerType(getHandlerType());
		        }
	        }
	        LOGGER.info(toString());
	        // don't need rest at this moment
		} catch (Exception e){
			throw e;
		}
    }
	
	public String toString() {
		return "HdlrBox{ " +
	            "version=" + getVersion() +
	            ", componentType=" + componentType +
	            ", handlerType=" + handlerType + " }";
	}

}
