package com.thank.utils.email;

import java.util.ArrayList;
import java.util.List;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class EmailMessage 
{

	protected String _sFrom = null;
	protected List<String> _sTo = null;
	protected List<InternetAddress> _oToList = null;
	protected List<String> _sCc = null;
	protected List<InternetAddress> _oCcList = null;
	protected List<String> _sBcc = null;
	protected List<InternetAddress> _oBccList = null;
	protected String _sSubject = null;
	protected String _sBody = null;
	protected String _sAttachment = null;
	protected String _sBodyFile = null;
	public static InternetAddress _oMyAddress = null;

	public EmailMessage()
	{
	}


	public EmailMessage(String sFrom, List<String> sTo, String sSubject, String sBody, String sAttachment)
	{
		_sFrom = sFrom;
		_sTo = sTo;
		_sSubject = sSubject;
		_sBody = sBody;
		_sAttachment = sAttachment;
	}
	
	public EmailMessage(String sFrom, String sTo, String sSubject, String sBody, String sAttachment)
	{
		_sFrom = sFrom;
		_sTo = new ArrayList<String>();
		_sTo.add(sTo);
		_sSubject = sSubject;
		_sBody = sBody;
		_sAttachment = sAttachment;
	}


	public EmailMessage(String sFrom, List<String> sTo, String sSubject, String sBody)
	{
		_sFrom = sFrom;
		_sTo = sTo;
		_sSubject = sSubject;
		_sBody = sBody;
	}

	public EmailMessage(String sFrom, String sTo, String sSubject, String sBody)
	{
		_sFrom = sFrom;
		_sTo = new ArrayList<String>();
		_sTo.add(sTo);
		_sSubject = sSubject;
		_sBody = sBody;
	}
	
	public EmailMessage(List<String> sTo, String sSubject, String sBody)
	{
		_sTo = sTo;
		_sSubject = sSubject;
		_sBody = sBody;
	}

	public EmailMessage(String sTo, String sSubject, String sBody)
	{
		_sTo = new ArrayList<String>();
		_sTo.add(sTo);
		_sSubject = sSubject;
		_sBody = sBody;
	}
	
	public void setFrom(String sFrom)
	{
		_sFrom = sFrom;
	}

	public void setMyAddress(InternetAddress _oAddress)
	{
		_oMyAddress = _oAddress;
	}

	public InternetAddress getMyAddress()
	{
		return _oMyAddress;
	}

	public String getFrom()
	{
		return _sFrom;
	}

	public InternetAddress[] getFromAddress() throws AddressException
	{
		if (_sFrom == null)
			return null;
		InternetAddress[] _oAdd = {new InternetAddress(_sFrom)};
		return _oAdd;
	}

	public void setTo(String sTo)
	{
		if ( _sTo == null )
			_sTo = new ArrayList<String>();
		_sTo.add(sTo);
	}

	public void setTo(List<String> sTo)
	{
		_sTo = sTo;
	}


	public List<String> getTo()
	{
		return _sTo;
	}

	public String getBodyFile()
	{
		return _sBodyFile;
	}

	public void setBodyFile(String bodyFile)
	{
		_sBodyFile = bodyFile;
	}

	public InternetAddress[] getToAddress() throws AddressException
	{
		if (_sTo == null)
			return null;
		if ( _sTo.size() == 0 )
			return null;
		if (_oToList == null )
		{
			_oToList = new ArrayList<InternetAddress>();
			int size = _sTo.size();
			for ( int i = 0 ; i < size ; i++ )
			{
				String str = (String)_sTo.get(i);
				_oToList.add(new InternetAddress(str));
			}
			
		}
			int size = _sTo.size();
			InternetAddress [] add = new InternetAddress [size];
			for ( int i = 0 ; i < size ; i++ )
			{
				String str = (String)_sTo.get(i);
				add[i] = new InternetAddress(str);
				if ( add[i] == null )
					System.out.println("add is null!!!");
			}
			return add;
		
	}

	public void setSubject(String sSubject)
	{
		_sSubject = sSubject;
	}

	public String getSubject()
	{
		return _sSubject;
	}

	public void setBody(String sBody)
	{
		_sBody = sBody;
	}

	public String getBody()
	{
		return _sBody;
	}

	public void setAttachment(String sAttachment)
	{
		_sAttachment = sAttachment;
	}

	public String getAttachment()
	{
		return _sAttachment;
	}

	public void setCc(List<String> sCc)
	{
		_sCc = sCc;
	}
	
	public void setCc(String sCc)
	{
		_sCc = new ArrayList<String>();
		_sCc.add(sCc);
	}

	public List<String> getCc()
	{
		return _sCc;
	}

	public InternetAddress[] getCcAddress() throws AddressException
	{
		if ( _sCc == null )
			return null;
		if ( _sCc.size() == 0 )
			return null;

		if (_oCcList == null )
		{
			_oCcList = new ArrayList<InternetAddress>();
			if (_sCc == null)
				return null;
			int size = _sCc.size();
			for ( int i = 0 ; i < size ; i++ )
			{
				_oCcList.add(new InternetAddress((String)_sCc.get(i)));
			}
		}
			int size = _sCc.size();
			InternetAddress [] add = new InternetAddress [size];
			for ( int i = 0 ; i < size ; i++ )
			{
				String str = (String)_sCc.get(i);
				add[i] = new InternetAddress(str);
				if ( add[i] == null )
					System.out.println("add is null!!!");
			}
			return add;
	}
	

	public void setBcc(List<String> sBcc)
	{
		_sBcc = sBcc;
	}

	public void setBcc(String sBcc)
	{
		_sBcc = new ArrayList<String>();
		_sBcc.add(sBcc);	
	}
	
	public List<String> getBcc()
	{
		return _sBcc;
	}
	
	public InternetAddress[] getBccAddress() throws AddressException
	{
		if ( _sBcc == null )
			return null;
		if ( _sBcc.size() == 0 )
			return null;
		if (_oBccList == null )
		{
			_oBccList = new ArrayList<InternetAddress>();
			if (_sBcc == null)
				return null;
			int size = _sBcc.size();
			for ( int i = 0 ; i < size ; i++ )
			{
				_oBccList.add(new InternetAddress((String)_sBcc.get(i)));
			}
		}

		return ( InternetAddress[] ) _oBccList.toArray();
	}

	public boolean hasAttachment()
	{
		if (_sAttachment == null)
			return false;
		return true;
	}


}