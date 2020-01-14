package com.example.pawan.authuser;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.pawan.authuser.Adapter.ListUsersAdapter;
import com.example.pawan.authuser.Common.Common;
import com.example.pawan.authuser.Holder.QBChatDialogHolder;
import com.example.pawan.authuser.Holder.QBChatMessagesHolder;
import com.example.pawan.authuser.Holder.QBUsersHolder;
import com.quickblox.chat.QBChatService;
import com.quickblox.chat.QBRestChatService;
import com.quickblox.chat.QBSystemMessagesManager;
import com.quickblox.chat.listeners.QBChatDialogParticipantListener;
import com.quickblox.chat.model.QBChatDialog;
import com.quickblox.chat.model.QBChatMessage;
import com.quickblox.chat.model.QBDialogType;
import com.quickblox.chat.model.QBPresence;
import com.quickblox.chat.request.QBDialogRequestBuilder;
import com.quickblox.chat.utils.DialogUtils;
import com.quickblox.core.QBEntityCallback;
import com.quickblox.core.exception.QBResponseException;
import com.quickblox.core.request.QBRequestGetBuilder;
import com.quickblox.users.QBUsers;
import com.quickblox.users.model.QBUser;

import org.jivesoftware.smack.SmackException;

import java.util.ArrayList;
import java.util.List;

public class ListUsersActivity extends AppCompatActivity {

    ListView lstusers;
    Button btncreatechat;

    private static  int checkpermission=0;

    String mode="";
    QBChatDialog qbChatDialog;
    List<QBUser> userAdd=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_users);

        mode=getIntent().getStringExtra(Common.UPDATE_MODE);
        qbChatDialog=(QBChatDialog)getIntent().getSerializableExtra(Common.UPDATE_DIALOG_EXTRA);


        
        lstusers=(ListView)findViewById(R.id.lstUsers);
        lstusers.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        
        btncreatechat=(Button)findViewById(R.id.btn_create_chat);
        btncreatechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //modify code
                if (mode == null) {

                    int countchoice = lstusers.getCount();

                    //CHECKING IF USER SELECT ONE USER THEN ITS PRIVATE CHAT ELSE MORE THAN ONE THEN IT WILL BE GROUP CHAT
                    if (lstusers.getCheckedItemPositions().size() == 1)
                        createPrivateChat(lstusers.getCheckedItemPositions());
                    else if (lstusers.getCheckedItemPositions().size() > 1)
                        createGroupChat(lstusers.getCheckedItemPositions());
                    else
                        Toast.makeText(ListUsersActivity.this, "Please Select Friend to chat", Toast.LENGTH_LONG).show();

                }
                else if(mode.equals(Common.UPDATE_ADD_MODE) && qbChatDialog!=null)
                {
                    if(userAdd.size()>0)
                    {
                        QBDialogRequestBuilder requestBuilder = new QBDialogRequestBuilder();

                        int cntChoice=lstusers.getCount();
                        SparseBooleanArray checkItemPositions = lstusers.getCheckedItemPositions();
                        for(int i=0;i<cntChoice;i++)
                        {
                            if(checkItemPositions.get(i))
                            {
                                QBUser user = (QBUser)lstusers.getItemAtPosition(i);
                                requestBuilder.addUsers(user);
                            }
                        }

                        //call services
                        QBRestChatService.updateGroupChatDialog(qbChatDialog,requestBuilder)
                                .performAsync(new QBEntityCallback<QBChatDialog>() {
                                    @Override
                                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                                        Toast.makeText(getBaseContext(),"Add User Succesfully",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(QBResponseException e) {
                                        Toast.makeText(getBaseContext(), "Error", Toast.LENGTH_SHORT).show();

                                    }
                                });
                    }
                }
                else if(mode.equals(Common.UPDATE_REMOVE_MODE)&&qbChatDialog!=null)
                {
                    if(userAdd.size()>0)
                    {
                        QBDialogRequestBuilder requestBuilder = new QBDialogRequestBuilder();

                        int cntChoice=lstusers.getCount();
                        SparseBooleanArray checkItemPositions = lstusers.getCheckedItemPositions();
                        for(int i=0;i<cntChoice;i++)
                        {
                            if(checkItemPositions.get(i))
                            {
                                QBUser user = (QBUser)lstusers.getItemAtPosition(i);
                                requestBuilder.removeUsers(user);
                            }
                        }

                        //call services
                        QBRestChatService.updateGroupChatDialog(qbChatDialog,requestBuilder)
                                .performAsync(new QBEntityCallback<QBChatDialog>() {
                                    @Override
                                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                                        Toast.makeText(getBaseContext(),"Remove User Succesfully",Toast.LENGTH_SHORT).show();
                                        finish();
                                    }

                                    @Override
                                    public void onError(QBResponseException e) {
                                        Toast.makeText(getBaseContext(), "Error In removing user", Toast.LENGTH_SHORT).show();

                                    }
                                });

                    }
                }
            }

        });

        if(mode==null && qbChatDialog == null)
            retrieveALLUser();
        else
            if(mode.equals(Common.UPDATE_ADD_MODE))
                loadListAvailableUser();
            else if(mode.equals(Common.UPDATE_REMOVE_MODE))
                loadListUsersInGroup();

        
    }

    //GROUP CHAT
    private void createGroupChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mDialog = new ProgressDialog(ListUsersActivity.this);
        mDialog.setMessage("Please waiting.......");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countchoices = lstusers.getCount();
        ArrayList<Integer> occupantIdsList = new ArrayList<>();
        for(int i=0;i<countchoices;i++)
        {
            if(checkedItemPositions.get(i));
            {
                QBUser user =(QBUser)lstusers.getItemAtPosition(i);

                    occupantIdsList.add(user.getId());

            }
        }

        //CREATE CHAT DIALOG

        QBChatDialog dialog =new QBChatDialog();
        dialog.setName(Common.createChatDialogName(occupantIdsList));
        dialog.setType(QBDialogType.GROUP);
        dialog.setOccupantsIds(occupantIdsList);


        QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
            @Override
            public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                mDialog.dismiss();
                Toast.makeText(getBaseContext(),"Create chat dialog succesfully",Toast.LENGTH_SHORT).show();

                //send system messages to recipent id user
                QBSystemMessagesManager qbSystemMessagesManager=QBChatService.getInstance().getSystemMessagesManager();
                QBChatMessage qbChatMessage = new QBChatMessage();
                qbChatMessage.setBody(qbChatDialog.getDialogId());
                for(int i=0;i<qbChatDialog.getOccupants().size();i++)
                {
                    //setting recipent id as all id of users in group
                    qbChatMessage.setRecipientId(qbChatDialog.getOccupants().get(i));
                    //sending each user system message
                    try {
                        qbSystemMessagesManager.sendSystemMessage(qbChatMessage);
                    } catch (SmackException.NotConnectedException e) {
                        e.printStackTrace();
                    }
                }
                finish();
            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR",e.getMessage());

            }
        });

    }

    /*private void userpermission(final QBChatDialog qbChatDialog) {
        QBChatDialogParticipantListener  participantListener = new QBChatDialogParticipantListener() {
            @Override
            public void processPresence(String dialogId, QBPresence qbPresence) {
                dialogId=qbChatDialog.getDialogId();
                QBRestChatService.getChatDialogById(dialogId).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        LayoutInflater inflater = LayoutInflater.from(getBaseContext());
                        View view = inflater.inflate(R.layout.dialog_get_permission,null);
                        AlertDialog.Builder alertDialogBuilder =new AlertDialog.Builder(getBaseContext());
                        alertDialogBuilder.setView(view);
                        alertDialogBuilder.setCancelable(true)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkpermission=1;
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        checkpermission=0;
                                    }
                                });

                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("Error Per",e.getMessage());

                    }
                });

            }
        };
        qbChatDialog.addParticipantListener(participantListener);
    }*/

    //PRIVATE CHAT
    private void createPrivateChat(SparseBooleanArray checkedItemPositions) {
        final ProgressDialog mDialog = new ProgressDialog(ListUsersActivity.this);
        mDialog.setMessage("Please waiting.......");
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();

        int countchoices = lstusers.getCount();
        for(int i=0;i<countchoices;i++)
        {
            if(checkedItemPositions.get(i));
            {
                final QBUser user =(QBUser)lstusers.getItemAtPosition(i);
                QBChatDialog dialog = DialogUtils.buildPrivateDialog(user.getId());

                QBRestChatService.createChatDialog(dialog).performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        mDialog.dismiss();
                        Toast.makeText(getBaseContext(),"Create private chat dialog succesfully",Toast.LENGTH_SHORT).show();

                        //send system message to recipent Id user
                        QBSystemMessagesManager qbSystemMessagesManager=QBChatService.getInstance().getSystemMessagesManager();
                        QBChatMessage qbChatMessage = new QBChatMessage();
                        qbChatMessage.setRecipientId(user.getId());
                        qbChatMessage.setBody(qbChatDialog.getDialogId());
                        try {
                            qbSystemMessagesManager.sendSystemMessage(qbChatMessage);
                        } catch (SmackException.NotConnectedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Log.e("ERROR",e.getMessage());

                    }
                });
            }
        }

    }

    private void loadListUsersInGroup() {
        btncreatechat.setText("Remove User");
        QBRestChatService.getChatDialogById(qbChatDialog.getDialogId())
                .performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        List<Integer> occupantsId = qbChatDialog.getOccupants();
                        List<QBUser> listUserAlreadyInChatGroup = QBUsersHolder.getInstance().getUsersByIds(occupantsId);
                        ArrayList<QBUser> users =new ArrayList<QBUser>();
                        users.addAll(listUserAlreadyInChatGroup);

                        ListUsersAdapter adapter =new ListUsersAdapter(getBaseContext(),users);
                        lstusers.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        userAdd=users;
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(ListUsersActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

                    }
                });
    }

    private void loadListAvailableUser() {
        btncreatechat.setText("ADD USER");

        QBRestChatService.getChatDialogById(qbChatDialog.getDialogId())
                .performAsync(new QBEntityCallback<QBChatDialog>() {
                    @Override
                    public void onSuccess(QBChatDialog qbChatDialog, Bundle bundle) {
                        ArrayList<QBUser> listUsers = QBUsersHolder.getInstance().getAllUsers();
                        List<Integer> occupantsId = qbChatDialog.getOccupants();
                        List<QBUser> listUserAlreadyInChatGroup = QBUsersHolder.getInstance().getUsersByIds(occupantsId);

                        //remove all users already in chat group
                        for(QBUser user:listUserAlreadyInChatGroup)
                            listUsers.remove(user);
                        if(listUsers.size()>0){
                            ListUsersAdapter adapter =new ListUsersAdapter(getBaseContext(),listUsers);
                            lstusers.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                            userAdd=listUsers;
                        }
                    }

                    @Override
                    public void onError(QBResponseException e) {
                        Toast.makeText(ListUsersActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();


                    }
                });
    }

    //TO GET ALL USERS
    private void retrieveALLUser() {

        QBUsers.getUsers(null).performAsync(new QBEntityCallback<ArrayList<QBUser>>() {
            @Override
            public void onSuccess(ArrayList<QBUser> qbUsers, Bundle bundle) {


                    //ADDING TO CACHE
                QBUsersHolder.getInstance().putUsers(qbUsers);


                ArrayList<QBUser> qbUserWithoutCurrent=new ArrayList<QBUser>();
                for(QBUser user :qbUsers)
                {
                    if(!user.getLogin().equals(QBChatService.getInstance().getUser().getLogin()));
                        qbUserWithoutCurrent.add(user);
                }

                //CREATING LIST USERS ADAPTER TO CUSTOMIZE USER
                ListUsersAdapter adapter =new ListUsersAdapter(getBaseContext(),qbUserWithoutCurrent);
                lstusers.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }

            @Override
            public void onError(QBResponseException e) {
                Log.e("ERROR",e.getMessage());

            }
        });

    }
}
