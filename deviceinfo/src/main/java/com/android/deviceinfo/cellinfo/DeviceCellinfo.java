package com.android.deviceinfo.cellinfo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.telephony.CellIdentityCdma;
import android.telephony.CellIdentityLte;
import android.telephony.CellIdentityNr;
import android.telephony.CellIdentityTdscdma;
import android.telephony.CellIdentityWcdma;
import android.telephony.CellInfo;
import android.telephony.CellInfoCdma;
import android.telephony.CellInfoGsm;
import android.telephony.CellInfoLte;
import android.telephony.CellInfoNr;
import android.telephony.CellInfoTdscdma;
import android.telephony.CellInfoWcdma;
import android.telephony.CellSignalStrengthCdma;
import android.telephony.CellSignalStrengthGsm;
import android.telephony.CellSignalStrengthLte;
import android.telephony.CellSignalStrengthNr;
import android.telephony.CellSignalStrengthTdscdma;
import android.telephony.TelephonyManager;
import android.util.Log;

import androidx.core.content.ContextCompat;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DeviceCellinfo {
    private static String TAG = "DeviceCellinfo";

    public Map<String, String> getCellinfo(Context context) {
        Map<String, String> cellinfoMap = new HashMap<>();
        if (!checkPhoneLocationPermission(context)) {
            Log.e(TAG, "no phone location permission");
            return cellinfoMap;
        }
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        List<CellInfo> cellInfoList = telephonyManager.getAllCellInfo();

        for (CellInfo cellInfo : cellInfoList) {
            if (!cellInfo.isRegistered()) {
                continue;
            }
            if (cellInfo instanceof CellInfoGsm) {
                CellInfoGsm cellInfoGsm = (CellInfoGsm) cellInfo;
                cellinfoMap.put("type", String.valueOf(1));
                cellinfoMap.put("registered", String.valueOf(cellInfoGsm.isRegistered() ? 1 : 0));
                cellinfoMap.put("lac", String.valueOf(cellInfoGsm.getCellIdentity().getLac()));
                cellinfoMap.put("cid", String.valueOf(cellInfoGsm.getCellIdentity().getCid()));
                cellinfoMap.put("arfcn", String.valueOf(cellInfoGsm.getCellIdentity().getArfcn()));
                cellinfoMap.put("bsic", String.valueOf(cellInfoGsm.getCellIdentity().getBsic()));
                cellinfoMap.put("mcc", String.valueOf(cellInfoGsm.getCellIdentity().getMcc()));
                cellinfoMap.put("mnc", String.valueOf(cellInfoGsm.getCellIdentity().getMnc()));
                cellinfoMap.put("ss", String.valueOf(cellInfoGsm.getCellSignalStrength()));

                CellSignalStrengthGsm cellSignalStrengthGsm = cellInfoGsm.getCellSignalStrength();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("status", String.valueOf(cellInfoGsm.getCellConnectionStatus()));
                    cellinfoMap.put("alphal", String.valueOf(cellInfoGsm.getCellIdentity().getOperatorAlphaLong()));
                    cellinfoMap.put("alphas", String.valueOf(cellInfoGsm.getCellIdentity().getOperatorAlphaShort()));
                    cellinfoMap.put("ta", String.valueOf(cellSignalStrengthGsm.getTimingAdvance()));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellinfoMap.put("ber", String.valueOf(cellSignalStrengthGsm.getBitErrorRate()));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    cellinfoMap.put("rssi", String.valueOf(cellSignalStrengthGsm.getRssi()));
                }
            } else if (cellInfo instanceof CellInfoLte) {
                CellInfoLte cellInfoLte = (CellInfoLte) cellInfo;
                CellIdentityLte cellIdentityLte = cellInfoLte.getCellIdentity();
                cellinfoMap.put("type", String.valueOf(3));
                cellinfoMap.put("registered", String.valueOf(cellInfoLte.isRegistered() ? 1 : 0));
                cellinfoMap.put("ci", String.valueOf(cellIdentityLte.getCi()));
                cellinfoMap.put("pci", String.valueOf(cellIdentityLte.getPci()));
                cellinfoMap.put("tac", String.valueOf(cellIdentityLte.getTac()));
                cellinfoMap.put("earfcn", String.valueOf(cellIdentityLte.getEarfcn()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("bandwidth", String.valueOf(cellIdentityLte.getBandwidth()));
                }
                cellinfoMap.put("mcc", String.valueOf(cellIdentityLte.getMcc()));
                cellinfoMap.put("mnc", String.valueOf(cellIdentityLte.getMnc()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("alphal", String.valueOf(cellIdentityLte.getOperatorAlphaLong()));
                    cellinfoMap.put("alphas", String.valueOf(cellIdentityLte.getOperatorAlphaShort()));
                }
                CellSignalStrengthLte cellSignalStrengthLte = (CellSignalStrengthLte) cellInfoLte.getCellSignalStrength();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    cellinfoMap.put("rsrp", String.valueOf(cellSignalStrengthLte.getRsrp()));
                    cellinfoMap.put("rsrq", String.valueOf(cellSignalStrengthLte.getRsrq()));
                    cellinfoMap.put("rssnr", String.valueOf(cellSignalStrengthLte.getRssnr()));
                    cellinfoMap.put("cqi", String.valueOf(cellSignalStrengthLte.getCqi()));
                    cellinfoMap.put("ta", String.valueOf(cellSignalStrengthLte.getTimingAdvance()));
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    cellinfoMap.put("rssi", String.valueOf(cellSignalStrengthLte.getRssi()));
                }
            } else if (cellInfo instanceof CellInfoWcdma) {
                CellInfoWcdma cellInfoWcdma = (CellInfoWcdma) cellInfo;
                CellIdentityWcdma cellIdentityWcdma = (CellIdentityWcdma) cellInfoWcdma.getCellIdentity();
                cellinfoMap.put("type", String.valueOf(4));
                cellinfoMap.put("registered", String.valueOf(cellInfoWcdma.isRegistered() ? 1 : 0));
                cellinfoMap.put("lac", String.valueOf(cellIdentityWcdma.getLac()));
                cellinfoMap.put("cid", String.valueOf(cellIdentityWcdma.getCid()));
                cellinfoMap.put("psc", String.valueOf(cellIdentityWcdma.getPsc()));
                cellinfoMap.put("uarfcn", String.valueOf(cellIdentityWcdma.getUarfcn()));
                cellinfoMap.put("mcc", String.valueOf(cellIdentityWcdma.getMcc()));
                cellinfoMap.put("mnc", String.valueOf(cellIdentityWcdma.getMnc()));
                cellinfoMap.put("ss", String.valueOf(cellInfoWcdma.getCellSignalStrength()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("alphal", String.valueOf(cellIdentityWcdma.getOperatorAlphaLong()));
                    cellinfoMap.put("alphas", String.valueOf(cellIdentityWcdma.getOperatorAlphaShort()));
                }
            } else if (cellInfo instanceof CellInfoCdma) {
                CellInfoCdma cellInfoCdma = (CellInfoCdma) cellInfo;
                CellIdentityCdma cellIdentityCdma = cellInfoCdma.getCellIdentity();
                cellinfoMap.put("type", String.valueOf(2));
                cellinfoMap.put("registered", String.valueOf(cellInfoCdma.isRegistered() ? 1 : 0));
                cellinfoMap.put("nid", String.valueOf(cellIdentityCdma.getNetworkId()));
                cellinfoMap.put("sid", String.valueOf(cellIdentityCdma.getSystemId()));
                cellinfoMap.put("bid", String.valueOf(cellIdentityCdma.getBasestationId()));
                cellinfoMap.put("latitude", String.valueOf(cellIdentityCdma.getLatitude()));
                cellinfoMap.put("longitude", String.valueOf(cellIdentityCdma.getLongitude()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("alphal", String.valueOf(cellIdentityCdma.getOperatorAlphaLong()));
                    cellinfoMap.put("alphas", String.valueOf(cellIdentityCdma.getOperatorAlphaShort()));
                }
                CellSignalStrengthCdma cellSignalStrengthCdma = (CellSignalStrengthCdma) cellInfoCdma.getCellSignalStrength();
                cellinfoMap.put("cdmaDbm", String.valueOf(cellSignalStrengthCdma.getCdmaDbm()));
                cellinfoMap.put("cdmaEcio", String.valueOf(cellSignalStrengthCdma.getCdmaEcio()));
                cellinfoMap.put("evdoDbm", String.valueOf(cellSignalStrengthCdma.getEvdoDbm()));
                cellinfoMap.put("evdoEcio", String.valueOf(cellSignalStrengthCdma.getEvdoEcio()));
                cellinfoMap.put("evdoSnr", String.valueOf(cellSignalStrengthCdma.getEvdoSnr()));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoNr) {
                CellInfoNr cellInfoNr = (CellInfoNr) cellInfo;
                cellinfoMap.put("type", String.valueOf(6));

                cellinfoMap.put("status", String.valueOf(cellInfoNr.getCellConnectionStatus()));
                CellIdentityNr cellIdentityNr = (CellIdentityNr) cellInfoNr.getCellIdentity();
                cellinfoMap.put("registered", String.valueOf(cellInfoNr.isRegistered() ? 1 : 0));
                cellinfoMap.put("pci", String.valueOf(cellIdentityNr.getPci()));
                cellinfoMap.put("tac", String.valueOf(cellIdentityNr.getTac()));
                cellinfoMap.put("nrArfcn", String.valueOf(cellIdentityNr.getNrarfcn()));
                cellinfoMap.put("mcc", cellIdentityNr.getMccString());
                cellinfoMap.put("mnc", cellIdentityNr.getMncString());
                cellinfoMap.put("nci", String.valueOf(cellIdentityNr.getNci()));
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("alphal", String.valueOf(cellIdentityNr.getOperatorAlphaLong()));
                    cellinfoMap.put("alphas", String.valueOf(cellIdentityNr.getOperatorAlphaShort()));
                }
                CellSignalStrengthNr cellSignalStrengthNr = (CellSignalStrengthNr) cellInfoNr.getCellSignalStrength();
                cellinfoMap.put("csiRsrp", String.valueOf(cellSignalStrengthNr.getCsiRsrp()));
                cellinfoMap.put("csiRsrq", String.valueOf(cellSignalStrengthNr.getCsiRsrq()));
                cellinfoMap.put("csisinr", String.valueOf(cellSignalStrengthNr.getCsiSinr()));
                cellinfoMap.put("ssRsrp", String.valueOf(cellSignalStrengthNr.getSsRsrp()));
                cellinfoMap.put("ssRsrq", String.valueOf(cellSignalStrengthNr.getCsiRsrq()));
                cellinfoMap.put("ssSinr", String.valueOf(cellSignalStrengthNr.getSsSinr()));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q && cellInfo instanceof CellInfoTdscdma) {
                CellInfoTdscdma cellInfoTdscdma = (CellInfoTdscdma) cellInfo;
                CellIdentityTdscdma cellIdentityTdscdma = (CellIdentityTdscdma) cellInfoTdscdma.getCellIdentity();
                cellinfoMap.put("type", String.valueOf(5));
                cellinfoMap.put("registered", String.valueOf(cellInfoTdscdma.isRegistered() ? 1 : 0));
                cellinfoMap.put("status", String.valueOf(cellInfoTdscdma.getCellConnectionStatus()));
                cellinfoMap.put("mcc", cellIdentityTdscdma.getMccString());
                cellinfoMap.put("mnc", cellIdentityTdscdma.getMncString());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    cellinfoMap.put("alphal", String.valueOf(cellIdentityTdscdma.getOperatorAlphaLong()));
                    cellinfoMap.put("alphas", String.valueOf(cellIdentityTdscdma.getOperatorAlphaShort()));
                }
                cellinfoMap.put("lac", String.valueOf(cellIdentityTdscdma.getLac()));
                cellinfoMap.put("cid", String.valueOf(cellIdentityTdscdma.getCid()));
                cellinfoMap.put("cpid", String.valueOf(cellIdentityTdscdma.getCpid()));
                cellinfoMap.put("uarfcn", String.valueOf(cellIdentityTdscdma.getCpid()));
                CellSignalStrengthTdscdma cellSignalStrengthTdscdma = cellInfoTdscdma.getCellSignalStrength();
                cellinfoMap.put("rscp", String.valueOf(cellSignalStrengthTdscdma.getRscp()));
            }
            if (!cellinfoMap.isEmpty()) {
                break;
            }
        }
        return cellinfoMap;
    }

    private boolean checkPhoneLocationPermission(Context context) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
               == PackageManager.PERMISSION_DENIED) {
            return false;
        }  else {
            return true;
        }
    }
}
