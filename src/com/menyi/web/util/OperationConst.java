package com.menyi.web.util;

/**
 *
 * <p>Title: ������ʽ����</p>
 *
 * <p>Description: </p>
 *
 * <p>Copyright: Copyright (c) 2008</p>
 *
 * <p>Company: ������</p>
 *
 * @author ������
 * @version 1.0
 */
public interface OperationConst
{
    int OP_ADD            = 1;  //���Ӳ���
    int OP_UPDATE         = 2;  //�޸Ĳ���
    int OP_DELETE         = 3;  //ɾ������
    int OP_QUERY          = 4;  //��ѯ����
    int OP_DETAIL         = 5;  //��ϸ����
    int OP_ADD_PREPARE    = 6;  //����ǰ׼������
    int OP_UPDATE_PREPARE = 7;  //�޸�ǰ׼������
    int OP_CHECK          = 8;  //���
    int OP_CHECK_PREPARE  = 9;  //���ǰ��׼������
    int OP_BACKUPDB       = 12;  //�������ݿ����
    int OP_DRAFT          = 13; //��Ϊ�ݸ�
    int OP_CHECK_LIST     = 14; //���������ʾ
    int OP_QUOTE          = 15;  //���ò���
    int OP_COPY_PREPARE   = 16;  //����ǰ׼������
    int OP_COPY           =17;   //���Ʋ���
    int OP_PRINT          =18;   //��ӡ����
    int OP_UPDATE_ADD     =88;   //������ݴ���ִ���޸ģ�������ݲ�����ִ�����

    int OP_LOGIN=10;  //��½����
    int OP_LOGOUT=11; //�˳�����

    int OP_READ_OVER = 19 ;
    int OP_TABLE_VIEW_PREPARE = 20; //�Զ������ʾ����
    int OP_TABLE_VIEW = 21; //�Զ������ʾ����
    int OP_POPUP_SELECT = 22; //�Զ������ʾ����
    int OP_TABLE_VIEW_PREPARE_MYD = 23; //ѡ����ֶ���ʾ����
    int OP_TABLE_VIEW_QUERY_MYD = 24;//�������ѯ����
    int OP_EXTENDBUTTON_DEFINE = 25;//�Զ�����չ��ť����

    int OP_BUTTON_AUDITING	   = 26 ; //ģ���� ��ť���
    int OP_BUTTON_REV_AUDITING	   = 27 ; //ģ���� ��ť�����
    int OP_LANGUAGE_SETTING_PREPARE = 28 ; //��������ǰ
    int OP_LANGUAGE_SETTING = 29 ;		//��������

    int OP_DELETE_PREPARE        = 30;  //ɾ��ǰ׼������

    int OP_SCOPE_RIGHT_QUERY         = 31; // �û��ķ�ΧȨ�޿���ǰ�Ĳ���
    int OP_SCOPE_RIGHT_ADD_PREPARE         = 33; // �û��ķ�ΧȨ�޿���ǰ�Ĳ���
    int OP_SCOPE_RIGHT_UPDATE_PREPARE         = 35; // �û��ķ�ΧȨ�޿���ǰ�Ĳ���
    int OP_SCOPE_RIGHT_ADD           = 37; // �û��ķ�ΧȨ�޿�������
    int OP_SCOPE_RIGHT_UPDATE        = 38; // �û��ķ�ΧȨ�޿����޸�
    int OP_SCOPE_RIGHT_DELETE            = 41; // �û�����ʾȨ�޿�������
    
    int OP_SET_ALERT = 42 ;			//�����������
    int OP_CANCEL_ALERT = 43 ;		//ȡ����������

    int OP_MODULE_RIGHT_PREPARE         = 51;  //ģ��Ȩ��׼��
    int OP_MODULE_RIGHT    = 52;  //ģ��Ȩ���޸�
    int OP_UPLOAD_PREPARE = 53 ;  //�ϴ�֮ǰ
    int OP_UPLOAD = 54 ;			//�ϴ�
    
    int OP_UPGRADE_PREPARE    = 60;  //����׼��
    int OP_UPGRADE    = 61;  //����׼��
    

    int OP_SEND_PREPARE     =70;//��ʱ��Ϣ����ǰ׼������
    int OP_SEND             =71;//��ʱ��Ϣ���Ͳ���
    int OP_REVERT_PREPARE   =72;//��ʱ��Ϣ�ظ�ǰ����
    int OP_REVERT           =73;//��ʱ��Ϣ�ظ�����
    int OP_RECEIVE			=69;//������Ϣ

    int OP_BBS_MANAGE =74; //�û�����

    int OP_OA_JSON = 75;//���ɶ�̬������
    int OP_OA_GRID = 76;//���ɶ�̬�������
    int OP_OA_VIEW_SINLE = 77;//�鿴������¼
    int OP_OA_VIEW_GROUOP_QUERY = 78;//����������ѯ
    int Op_AUDITING=79;//���Ȩ��
    int Op_RETAUDITING=83;//�����
    int OP_AUDITING_PREPARE=82;//���ǰ����
    int OP_DOWNLOAD = 80 ;//����
    int frameLeft = 81 ;//����ҳ�����ת
    
    int OP_SET_PREPARE = 84 ;
    int OP_SET = 85 ;
    
    int OP_IMPORT_PREPARE = 91; //���ݵ���׼��
    int OP_IMPORT = 92;//���ݵ���
    int OP_READ = 93 ;//�İ�
    int OP_NEXT_NODE = 94 ;//��ȡ�¸���˽��
    
    int OP_URL_TO = 96;//ת��
    int OP_ERROR_LIST = 97;//���ʧ���û��б�
    int OP_STOCKDISTRIBUTING = 98; //�����ֲ���
    int OP_EXPORT = 99;//����
    
    int BATCHDELETE = 100;  //����ɾ��
    int OP_UPDATE_PWD_PREPARE = 101 ;//�޸�����
    int OP_UPDATE_PWD = 102 ;
    
    int OP_SELECT_PIC = 401;//ѡ��ͼƬ
    
    int OP_CASHIER_AUDIT = 99;//���
    
    int OP_CASHIER_REAUDIT = 100;//�����
    
    int OP_CASHIER_IMPORT_PRE = 101;//Excel��������
    
    int OP_CASHIER_IMPORT_DATA = 102;//����Excel����
    
    int OP_CASHIER_INIT_PRE = 103;//����ģ���ʼ��
    
    int OP_CASHIER_INIT = 104;//���ɳ�ʼ����������
    
    int OP_CASHIER_CASH = 105;//����-�ֽ��ռ���
    
    int OP_CASHIER_CASH_QUERY = 106;//����-�ֽ��ռ��˲�ѯ
    
    int OP_CASHIER_CASH_ADD = 107;//����-�ֽ��ռ������
    
    int OP_CASHIER_CASH_QUOTE = 108;//����-�ֽ��ռ�������
    
    int OP_CASHIER_CASH_GENBYSINGLE = 109;//����-�ֽ��ռ�������ƾ֤��������
    
    int OP_CASHIER_CASH_GENBYSUMMARY = 110;//����-�ֽ��ռ�������ƾ֤�����ܣ� 
    
    int OP_CASHIER_CASH_EDIT = 111;//����-�ֽ��ռ����޸�
    
    int OP_CASHIER_CASH_UPDATE = 112;//����-�ֽ��ռ����޸ı���

    int OP_CASHIER_CASH_DELETE = 113;//����-�ֽ��ռ���ɾ��
    
    int OP_CASHIER_CASH_EXPORT = 114;//����-�ֽ��ռ��˵���
    
    int OP_CASHIER_CASHACCOUNT_PRE = 115;//����-�ֽ���˵�          
    
    int OP_CASHIER_CASHACCOUNT = 116;//����-�ֽ���˵�    
    
    int OP_CASHIER_BANKACCOUNT_PRE = 119;//����-���д����˵�
    
    int OP_CASHIER_BANKACCOUNT = 120;//����-���д����˵���ѯ
    
    int OP_CASHIER_BANK = 121;//����-���д���ռ���
    
    int OP_CASHIER_BANK_QUERY = 122;//����-���д���ռ��˲�ѯ
    
    int OP_CASHIER_BANK_ADD = 123;//����-���д���ռ������
    
    int OP_CASHIER_BANK_QUOTE = 124;//����-�ֽ��ռ�������
    
    int OP_CASHIER_BANK_GENBYSINGLE = 125;//����-�����ռ�������ƾ֤��������
    
    int OP_CASHIER_BANK_GENBYSUMMARY = 126;//����-�����ռ�������ƾ֤�����ܣ�
    
    int OP_CASHIER_BANK_EXPORT = 131;//����-���д���excel
    
    int OP_CASHIER_BANK_DELETE = 129 ; //����-�����ռ���ɾ��
    
    int OP_CASHIER_BANK_EDIT = 127;//����-�����ռ����޸�
    
    int OP_CASHIER_BANK_UPDATE = 128;//����-�����ռ����޸ı���    
    
    int OP_CASHIER_DOWNLOAD = 132;//���d����
    
    int OP_CASHIER_BALANCE_PRE = 133;//�ռ����½�
    
    int OP_CASHIER_BALANCE = 134;//�½��ύ
    
    int OP_CASHIER_REBALANCE_PRE = 135;//�ռ��˷��½�
    
    int OP_CASHIER_REBALANCE = 136;//���½��ύ
    
    int OP_CASHIERSET_UPPRE= 137;//����Ȩ������
    int OP_CASHIERSET_SAVE= 138;//����Ȩ�ޱ���
    
    int OP_CASHIER_CASH_POST = 139;//���ɹ���
    int OP_CASHIER_CASH_REPOST = 140;//���ɷ�����
    
    String OP_GROUPID_ONE_NUM = "1";//�ռ���
    String OP_GROUPID_TWO_NUM = "2";//�ݸ���
    String OP_GROUPID_THREE_NUM = "3";//�ѷ����ʼ���
    String OP_GROUPID_FOUR_NUM = "4";//�����ʼ���
    String OP_GROUPID_FIVE_NUM = "5";//�ϼ���
    
    String OP_GROUPID_ONE_STR = "�ռ���";//1
    String OP_GROUPID_TWO_STR = "�ݸ���";//2
    String OP_GROUPID_THREE_STR = "�ѷ����ʼ���";//3
    String OP_GROUPID_FOUR_STR = "�����ʼ���";//4
    String OP_GROUPID_FIVE_STR = "�ϼ���";//5
    
    
    
}
