package horrarndoo.example.airpaypassdialog;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Horrarndoo on 2019/5/7.
 * <p>
 */
public class PayPassView extends RelativeLayout {
    private Activity mContext;//上下文
    private GridView gvPay; //支付键盘
    private String mPassword = "";//密码
    private TextView[] mTvPass;//密码数字控件
    private ImageView ivClose;//关闭
    private TextView tvForget;//忘记密码
    private TextView tvHint;//提示 (提示:密码错误,重新输入)
    private List<Integer> mNumbers;//1,2,3---0
    private View vPassword;//布局

    public interface OnPayClickListener {
        void onPassFinish(String passContent);

        void onPayClose();

        void onPayForget();
    }

    private OnPayClickListener mPayClickListener;

    public void setPayClickListener(OnPayClickListener listener) {
        mPayClickListener = listener;
    }

    public PayPassView(Context context) {
        super(context);
    }

    public PayPassView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PayPassView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = (Activity) context;

        initView();
        this.addView(vPassword);
    }

    private void initView() {
        vPassword = LayoutInflater.from(mContext).inflate(R.layout.layout_paypass, null);

        ivClose = vPassword.findViewById(R.id.iv_close);//关闭
        tvForget = vPassword.findViewById(R.id.tv_forget);//忘记密码
        tvHint = vPassword.findViewById(R.id.tv_hint);//提示文字
        mTvPass = new TextView[6]; //密码控件
        mTvPass[0] = vPassword.findViewById(R.id.tv_pass1);
        mTvPass[1] = vPassword.findViewById(R.id.tv_pass2);
        mTvPass[2] = vPassword.findViewById(R.id.tv_pass3);
        mTvPass[3] = vPassword.findViewById(R.id.tv_pass4);
        mTvPass[4] = vPassword.findViewById(R.id.tv_pass5);
        mTvPass[5] = vPassword.findViewById(R.id.tv_pass6);
        gvPay = vPassword.findViewById(R.id.gv_pass);

        ivClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                cleanAllTv();
                mPayClickListener.onPayClose();
            }
        });

        tvForget.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mPayClickListener.onPayForget();
            }
        });

        //初始化数据
        mNumbers = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            mNumbers.add(i);
        }
        mNumbers.add(10);
        mNumbers.add(0);
        mNumbers.add(R.mipmap.ic_pay_del0);

        gvPay.setAdapter(adapter);
    }

    BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return mNumbers.size();
        }

        @Override
        public Object getItem(int position) {
            return mNumbers.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_airpaypass, null);
                holder = new ViewHolder();
                holder.btnNumber = convertView.findViewById(R.id.btNumber);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //-------------设置数据----------------
            holder.btnNumber.setText(mNumbers.get(position) + "");
            if (position == 9) {
                holder.btnNumber.setText("");
                holder.btnNumber.setBackgroundColor(mContext.getResources().getColor(R.color.graye3));
            }
            if (position == 11) {
                holder.btnNumber.setText("");
                holder.btnNumber.setBackgroundResource(mNumbers.get(position));
            }

            if (position == 11) {
                holder.btnNumber.setOnTouchListener(new OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        if (position == 11) {
                            switch (event.getAction()) {
                                case MotionEvent.ACTION_DOWN:
                                    holder.btnNumber.setBackgroundResource(R.mipmap.ic_pay_del1);
                                    break;
                                case MotionEvent.ACTION_MOVE:
                                    holder.btnNumber.setBackgroundResource(R.mipmap.ic_pay_del1);
                                    break;
                                case MotionEvent.ACTION_UP:
                                    holder.btnNumber.setBackgroundResource(R.mipmap.ic_pay_del0);
                                    break;
                            }
                        }
                        return false;
                    }
                });
            }
            holder.btnNumber.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < 11 && position != 9) {//0-9按钮
                        if (mPassword.length() == 6) {
                            return;
                        } else {
                            mPassword = mPassword + mNumbers.get(position);//得到当前数字并累加
                            mTvPass[mPassword.length() - 1].setText("*"); //设置界面*
                            //输入完成
                            if (mPassword.length() == 6) {
                                mPayClickListener.onPassFinish(mPassword);//请求服务器验证密码
                            }
                        }
                    } else if (position == 11) {//删除
                        if (mPassword.length() > 0) {
                            mTvPass[mPassword.length() - 1].setText("");//去掉界面*
                            mPassword = mPassword.substring(0, mPassword.length() - 1);//删除一位
                        }
                    }
                    if (position == 9) {//空按钮
                    }
                }
            });

            return convertView;
        }
    };

    static class ViewHolder {
        public TextView btnNumber;
    }

    /**
     * 清楚所有密码TextView
     */
    public void cleanAllTv() {
        mPassword = "";
        for (int i = 0; i < 6; i++) {
            mTvPass[i].setText("");
        }
    }
}
