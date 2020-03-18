package com.example.health;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

public class ActivityImageSelection extends AppCompatActivity {

    private final int Pick_image = 1;

    private ImageView iv_userPicture;
    private GridView gv_imageSelection;

    private UserProfile userProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_selection);

        userProfile = UserProfile.getInstance();

        iv_userPicture = findViewById(R.id.iv_userPictureImSel);
        gv_imageSelection = findViewById(R.id.gv_imageSelection);
        gv_imageSelection.setAdapter(new AdapterImageSelection(this));
        gv_imageSelection.setOnItemClickListener(gridViewOnItemClickListener);

    }

    public void onClickSaveImSel(View v) {
        Bitmap bitmap = ((BitmapDrawable)iv_userPicture.getDrawable()).getBitmap();
        userProfile.setUserPicture(bitmap);
        finish();
    }

    public void onClickOpenGallery(View v) {
        //Вызываем стандартную галерею для выбора изображения с помощью Intent.ACTION_PICK:
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, Pick_image);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case Pick_image:
                if (resultCode == RESULT_OK) {

                    //Получаем URI изображения, преобразуем его в Bitmap
                    //объект и отображаем в элементе ImageView нашего интерфейса:
                    final Uri imageUri = imageReturnedIntent.getData();
                    iv_userPicture.setImageURI(imageUri);
                }
        }
    }

    private GridView.OnItemClickListener gridViewOnItemClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position,
                                long id) {

            ImageView imageView = v.findViewById(R.id.imageView);

            Uri imageUri = (new Uri.Builder())
                    .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                    .authority(getResources().getResourcePackageName((int)imageView.getTag()))
                    .appendPath(getResources().getResourceTypeName((int)imageView.getTag()))
                    .appendPath(getResources().getResourceEntryName((int)imageView.getTag()))
                    .build();
            iv_userPicture.setImageURI(imageUri);

        }
    };
}
