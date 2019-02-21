package com.example.edu;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.edu.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.FirebaseDatabase;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

//회원가입 Activity
public class RegisterActivity extends AppCompatActivity {

    Toolbar toolbar;
    private EditText etEmail, etName, etPassword, etPassword2, etAnswer;
    private RadioGroup rgGender;
    private RadioButton rbMan, rbWomen;
    private Button btnRegister;
    private Spinner spnQuestion;
    private ImageView ivUserPhoto, ivCheckEmail, ivCheckName, ivCheckPassword, ivCheckPassword2, ivCheckGender, ivCheckQ;

    private Uri photoUri; //사진을 저장할 경로
    private String currentPhotoPath;//실제 사진 파일 경로
    String mImageCaptureName;//사진 이름
    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private final int CAMERA_CODE = 1111;
    private static final int GALLERY_CODE = 1112;
    private final int REQUEST_PERMISSION_CODE = 100; //콜백함수 호출 시 requestCode로 넘어가는 구분자

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etEmail);
        etName = findViewById(R.id.etName);
        etPassword = findViewById(R.id.etPassword);
        etPassword2 = findViewById(R.id.etPassword2);
        etAnswer = findViewById(R.id.etAnswer);
        btnRegister = findViewById(R.id.btnRegister);
        spnQuestion = findViewById(R.id.spnQuestion);
        ivUserPhoto = findViewById(R.id.ivUserPhoto);
        ivCheckEmail = findViewById(R.id.ivCheckEmail);
        ivCheckName = findViewById(R.id.ivCheckName);
        ivCheckPassword = findViewById(R.id.ivCheckPassword);
        ivCheckPassword2 = findViewById(R.id.ivCheckPassword2);
        ivCheckGender = findViewById(R.id.ivCheckGender);
        ivCheckQ = findViewById(R.id.ivCheckQ);
        rgGender = findViewById(R.id.rgGender);
        rbMan = findViewById(R.id.rbMan);
        rbWomen = findViewById(R.id.rbWomen);

        //비밀번호 찾기 질문 spinner
        final String[] question = getResources().getStringArray(R.array.question);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, question);
        spnQuestion.setAdapter(adapter);

        toolbar = findViewById(R.id.tBar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); //뒤로가기 버튼 생성

        //회원가입 버튼 클릭 이벤트
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //빈 항목 체크
                if (validate() == false) {
                    return;
                } else {
                    RegisterEvent();
                }
            }
        });

        //editText 키 입력 리스너
        etEmail.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputEmail();
                return false;
            }
        });

        //editText 포커스 변경 리스너
        etEmail.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputEmail();
            }
        });

        etName.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputName();
                return false;
            }
        });

        etName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputName();
            }
        });

        etPassword.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputPassword();
                return false;
            }
        });

        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputPassword();
            }
        });

        etPassword2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputPassword2();
                return false;
            }
        });

        etPassword2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputPassword2();
            }
        });

        //radioButton 클릭 리스너
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                ivCheckGender.setImageResource(R.drawable.ic_check_black);
            }
        });

        etAnswer.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                checkInputAnswer();
                return false;
            }
        });

        etAnswer.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                checkInputAnswer();
            }
        });
    }

    //항목이 채워졌을 때 체크 이미지 색상 변경
    private void checkInputEmail() {
        String Email = etEmail.getText().toString();
        if (Email.isEmpty()) {
            ivCheckEmail.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckEmail.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputName() {
        String Name = etName.getText().toString();
        if (Name.isEmpty()) {
            ivCheckName.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckName.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputPassword() {
        String Password = etPassword.getText().toString();
        if (Password.isEmpty()) {
            ivCheckPassword.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckPassword.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputPassword2() {
        String Password2 = etPassword2.getText().toString();
        if (Password2.isEmpty()) {
            ivCheckPassword2.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckPassword2.setImageResource(R.drawable.ic_check_black);
        }
    }

    private void checkInputAnswer() {
        String Answer = etAnswer.getText().toString();
        if (Answer.isEmpty()) {
            ivCheckQ.setImageResource(R.drawable.ic_check_gray);
        } else {
            ivCheckQ.setImageResource(R.drawable.ic_check_black);
        }
    }

    //카메라에서 사진 촬영
    public void takePhoto() {
        //디바이스 장치의 내장 경로의 상태
        String state = Environment.getExternalStorageState();
        //SD CARD를 읽고 쓸 수 있는 상태인지 확인
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            //카메라 호출
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                File photoFile = null;

                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                }

                if (photoFile != null) {
                    //해당 파일 위치에 있는 contents provider 값을 얻어옴
                    photoUri = FileProvider.getUriForFile(this, "com.example.edu.fileprovider", photoFile);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(intent, CAMERA_CODE);
                }
            }
        }
    }

    //앨범에서 사진 가져오기
    public void takeAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        switch (requestCode) {
            case GALLERY_CODE:
                sendPicture(data.getData());
                break;
            case CAMERA_CODE: {
                getPictureForPhoto();
                break;
            }
            default:
                break;
        }
    }

    //imageView 클릭 이벤트 메소드
    public void onClick(View v) {
        //CAMERA,WRITE_EXTERNAL_STORAGE 권한 승인일 경우
        if(requestPermission() == true){
            DialogInterface.OnClickListener cameraListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    takePhoto();
                }
            };

            DialogInterface.OnClickListener albumListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    takeAlbum();
                }
            };

            new AlertDialog.Builder(this)
                    .setTitle("학생증 이미지 선택")
                    .setPositiveButton("사진촬영", cameraListener)
                    .setNeutralButton("앨범선택", albumListener)
                    .show();
        }
    }

    public boolean requestPermission() {
        //권한이 필요한지 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            //사용자가 권한을 승인했는지 확인
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                //권한 요청 dialog
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            }
        } else {
        }
        return true;
    }

    //권한 요청 결과에 따른 콜백 함수
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_PERMISSION_CODE == requestCode) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this,"권한이 승인되었습니다",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"권한이 거절되었습니다. 권한을 승인해주세요",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                //뒤로가기 버튼 클릭 시 로그인 화면 연결
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    void RegisterEvent() { // 회원가입이 정상적으로 됐는지 확인해주고 다음 화면으로 넘겨줌. 확인하고 넘겨주는 이 2가지를 분리할 예정. LoginActivity 처럼.

            FirebaseAuth.getInstance()
                    .createUserWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString())
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //회원가입을 할 때 이름이 들어가게 되는 곳이다. 아래 두 줄의 코드는 push 메세지 기능을 위해 작성하였다
                            try {
                                UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder().setDisplayName(etName.getText().toString()).build();
                                task.getResult().getUser().updateProfile(userProfileChangeRequest);
                            } catch (Exception e){
                            }

                            if (!task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "회원가입 오류 : " + task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(getApplicationContext(), "회원가입이 완료 되었습니다.", Toast.LENGTH_LONG).show();


                                int id = rgGender.getCheckedRadioButtonId();
                                RadioButton rb = findViewById(id); // 라디오 버튼값 획득

                                UserModel userModel = new UserModel();
                                userModel.userName = etName.getText().toString();
                                userModel.userGender = rb.getText().toString();
                                userModel.userPwQuestion = spnQuestion.getSelectedItem().toString();
                                userModel.userPwAnswer = etAnswer.getText().toString();
                                //userModel.userFavorites = 별 누르면 그룹이 관심목록에 표시 clickListener > 파베에 group Uid가 userFavorites에 추가 > 이것을 관심목록에 표시
                                userModel.uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                //회원가입 할 때마다 uid가 담겨서 회원가입이 된다.
                                //이 uid를 통해 내가 원하는 사람이랑 채팅을 할 수 있게 된다.

                                String uid = task.getResult().getUser().getUid();
                                FirebaseDatabase.getInstance().getReference().child("users").child(uid).setValue(userModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        finish();
                                    }
                                });
                            }
                        }
                    });
    }

    //빈 항목 체크 메소드
    private boolean validate() {
        boolean valid = true;
        String email, name, password, password2, answer;
        email = etEmail.getText().toString();
        name = etName.getText().toString();
        password = etPassword.getText().toString();
        password2 = etPassword2.getText().toString();
        answer = etAnswer.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Email를 입력해 주세요!");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (rbMan.isChecked() == false && rbWomen.isChecked() == false) {
            rbWomen.setError("성별을 선택하세요!");
            valid = false;
        } else {
            rbWomen.setError(null);
        }

        if(!password.equals(password2)){
            etPassword2.setError("비밀번호가 일치하지 않습니다!");
            valid = false;
        } else {
            etPassword2.setError(null);
        }

        if (name.isEmpty()) {
            etName.setError("이름을 입력해 주세요!");
            valid = false;
        } else {
            etName.setError(null);
        }

        if (password.isEmpty()) {
            etPassword.setError("Password를 입력해 주세요!");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        if (password2.isEmpty()) {
            etPassword2.setError("Password를 입력해 주세요!");
            valid = false;
        } else {
            etPassword2.setError(null);
        }
        if (answer.isEmpty()) {
            etAnswer.setError("답변을 입력해 주세요!");
            valid = false;
        } else {
            etAnswer.setError(null);
        }
        return valid;
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한 번 더 누르시면 로그인 화면으로 돌아갑니다", Toast.LENGTH_SHORT).show();
        }
    }

    private File createImageFile() throws IOException {
        File dir = new File(Environment.getExternalStorageDirectory() + "/eduPicture/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        mImageCaptureName = timeStamp + ".png";

        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsoluteFile() + "/eduPicture/" + mImageCaptureName);
        currentPhotoPath = storageDir.getAbsolutePath();

        return storageDir;
    }

    //카메라에서 사진찍은 후 정방향 처리
    private void getPictureForPhoto() {
        //사진이 회전되어 출력하는 경우 상황에 맞게 회전
        Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(currentPhotoPath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation;
        int exifDegree;

        if (exif != null) {
            exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            exifDegree = exifOrientationToDegrees(exifOrientation);
        } else {
            exifDegree = 0;
        }

        ivUserPhoto.setImageBitmap(rotate(bitmap, exifDegree));
    }

    //앨범에서 사진선택 후 정방향 처리
    private void sendPicture(Uri imgUri) {
        String imagePath = getRealPathFromURI(imgUri);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        ExifInterface exif = null;

        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        ivUserPhoto.setImageBitmap(rotate(bitmap, exifDegree));
    }

    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }

        return 0;
    }

    private Bitmap rotate(Bitmap src, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index = 0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);

        if (cursor.moveToFirst()) {
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }

}
