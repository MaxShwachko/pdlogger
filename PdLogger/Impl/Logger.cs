using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading;
using PdLogger.Core;
using PdLogger.Net;
using PdLogger.Net.Impl;
using UnityEngine;

namespace PdLogger.Impl
{
    public class Logger : MonoBehaviour, ILogger
    {
	    public string sessionSaveKey;
	    public string authToken;
	    public string apiVersion;
	    public string host;
	    public int port;
	    public NetProtocol netProtocol;

		public string source;
		
		private bool _hasException;
		
		private readonly List<string> _deniedTypes = new List<string>();

		private ALoggerNetClient _loggerNetClient;
		private ILogLevelConverter _logLevelConverter;

		public ALoggerNetClient ALoggerNetClient => _loggerNetClient;

		private void Start()
		{
			DontDestroyOnLoad(gameObject);
			
			switch (netProtocol)
			{
				case NetProtocol.Tcp:
					_loggerNetClient = new TcpLoggerNetClient(authToken, apiVersion, host, port);
					break;
				case NetProtocol.Udp:
					_loggerNetClient = new UdpLoggerNetClient(authToken, apiVersion, host, port);
					break;
				default:
					throw new ArgumentOutOfRangeException();
			}

			var sessionNumber = PlayerPrefs.GetInt(sessionSaveKey, 0);
			sessionNumber++;
			PlayerPrefs.SetInt(sessionSaveKey, sessionNumber);
			PlayerPrefs.Save();
			
			_loggerNetClient.Connect(source, sessionNumber);
			
			Application.logMessageReceived += LogCallback;
		}

		private void OnDestroy()
		{
			Application.logMessageReceived -= LogCallback;
			_loggerNetClient.Destroy();
		}

		public ILogger SetLogLevelConverter(ILogLevelConverter logLevelConverter)
		{
			_logLevelConverter = logLevelConverter;
			return this;
		}

		public ILogger AddDeniedType<T>()
		{
			var type = typeof(T);
			if (!_deniedTypes.Contains(type.Name))
			{
				_deniedTypes.Add(type.Name);
			}

			return this;
		}

		public ILogger AddCustomName(string customName)
		{
			if (!_deniedTypes.Contains(customName))
			{
				_deniedTypes.Add(customName);
			}

			return this;
		}

		public ILogger AddNamespace(string @namespace)
		{
			var types = AppDomain.CurrentDomain.GetAssemblies()
				.SelectMany(t => t.GetTypes())
				.Where(t => t.IsClass && t.Namespace == @namespace);

			foreach (var type in types)
			{
				if (!_deniedTypes.Contains(type.Name))
				{
					_deniedTypes.Add(type.Name);
				}
			}

			return this;
		}

		private void LogCallback(string message, string stackTrace, LogType logType)
		{
			if(!_loggerNetClient.IsConnected)
				return;
			
			if (_hasException)
				return;

			if (logType == LogType.Exception)
				_hasException = true;

			var logLevel = _logLevelConverter.ConvertTo(logType);
			var builder = new MessageBuilder(message, logLevel);
			builder.AddAdditionalField("stack_trace", stackTrace);
			
			var mes = builder.ToMessage();
			ThreadPool.QueueUserWorkItem(_ => TrySendLog(mes, logType));
		}

		private void TrySendLog(Message mes, LogType logType)
		{
			if (logType == LogType.Log)
			{
				var extractedType = ExtractTypeFromLogMessage(mes.ShortMessage);
				mes.AdditionalFields.Add("class_type", extractedType);
				if (string.IsNullOrEmpty(extractedType) || _deniedTypes.Contains(extractedType))
					return;
			}

			if (logType == LogType.Warning)
				return;

			_loggerNetClient.Send(mes);
		}
		
		private string ExtractTypeFromLogMessage(string message)
		{
			var startIndex = message.IndexOf("[", StringComparison.Ordinal);
			if (startIndex == -1) return null;

			var endIndex = message.IndexOf("]", StringComparison.Ordinal);
			if (endIndex == -1) return null;

			return message.Substring(startIndex + 1, endIndex - startIndex - 1);
		}
    }

    public enum NetProtocol
    {
	    Tcp, Udp
    }
}