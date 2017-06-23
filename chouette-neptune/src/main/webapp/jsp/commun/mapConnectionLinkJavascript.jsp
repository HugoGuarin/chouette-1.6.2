<%@ taglib prefix="s" uri="/struts-tags"%>

<s:include value="/jsp/commun/map.jsp" />

<s:if test="baseLayerSource != '' && ( baseLayerSource != 'geoportal' || geoportalApiKey != '')">
  <script language="JavaScript" type="text/javascript" src="<s:url value='/js/map/markerLayer/showMarkerLayer.js' includeParams='none'/>" ></script>
  <script language="JavaScript" type="text/javascript" src="<s:url value='/js/map/mapType/mapConnectionLink.js' includeParams='none'/>" ></script>
</s:if>